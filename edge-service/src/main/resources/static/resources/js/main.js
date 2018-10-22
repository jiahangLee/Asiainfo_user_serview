function getOauthTokenFromStorage() {
    return localStorage.getItem('token');
}

function removeOauthTokenFromStorage() {
    return localStorage.removeItem('token');
}

function getCurrentAccount(callback) {

    var token = getOauthTokenFromStorage();
    var account = null;

    if (token) {
        $.ajax({
            url: 'accounts/current',
            datatype: 'json',
            type: 'get',
            headers: {'Authorization': 'Bearer ' + token},
            async: false,
            success: function (data) {
                account = data;
                callback(account);
            },
            error: function () {
                removeOauthTokenFromStorage();
                callback();
            }
        });
    }else{
        callback(null);
    }

}
function logout() {
    removeOauthTokenFromStorage();
    location.reload();
}

$(window).load(function(){


    getCurrentAccount(function(account){
        if(account){
            showGreetingPage(account.name);
        }else{
            showLoginForm();
        }
    });

    $("#loginButton").on("click", function(){
        login();
    });

    $("#logoutButton").on("click",function () {
        logout();
    })

})


function showGreetingPage(name) {
    $("#greetings").html("欢迎"+name);
    $("#loginForm").hide();
    $("#greetingDiv").show();
}

function showLoginForm() {
    $("#loginForm").show();
    $("#greetingDiv").hide();


}

function login() {



    var username = $("input[id='username']").val();
    var password = $("input[id='password']").val();
    requestOauthToken(username, password, function(){
        showGreetingPage(username);
    },function(){
        alert("Something went wrong. Please, check your credentials");
    })

}

function requestOauthToken(username, password,successCB,errorCB) {
    var shaObj = new jsSHA("SHA-1", "TEXT");
    shaObj.update(password);

    var hash = shaObj.getHash("HEX");
    $.ajax({
        url: 'uaa/oauth/token',
        datatype: 'json',
        type: 'post',
        async: false,
        data: {
            scope: 'ui',
            username: username,
            password: hash,
            grant_type: 'password',
            client_id:'browser'

        },
        success: function (data) {
            localStorage.setItem('token', data.access_token);
            successCB();
        },
        error: function (err) {
            removeOauthTokenFromStorage();
            errorCB();
        }
    });




}