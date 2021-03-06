// Piggy spin first time
function spinThatShit(){
    spinFirstTime();
    setTimeout(spinFirstTime, 1900);
}

// Piggy on hover
function spinAgain() {
    spin();
    setTimeout(spin, 1700);
}

function spinFirstTime() {
    $("#piggy").toggleClass("spinthatshit");
}

function spin() {
    $("#piggy").toggleClass("spinagain");
}

function toggleInfo() {
    $("#infopage").toggle();
}

// Flip login/password form
function flipForm() {
    $("#cube").toggleClass("flippedform");
    $("#frontpasswordform").focus();
}

// Spin piggy on hover
$("#piggy").on("click mouseover", function(){
    if ($(this).hasClass("spinthatshit") === false && $(this).hasClass("spinagain") === false) {
        spinAgain();
    }
});

// Flip to reg card
$(".fliptext").bind("click", function(){
    setTimeout( function() { $("#plusavatar").addClass("avataranimation"); } , 1000);
    $("#flipper").toggleClass("flippedcard");
});

// Flip to info card
$(".flipinfo").on("click", function() {
    $("#flipper").toggleClass("flippedcardinfo");
    toggleInfo();
});

// Flip from info card
$(".frominfo, #infotitle, #infosubtitle").on("click", function() {
    $("#flipper").toggleClass("flippedcardinfo");
    setTimeout(toggleInfo, 400);
});

$("#enter").on("click", function() {flipForm()});
$("#secondenter").on("click", function() {checkData()});

// Forms handlers
$("#frontloginform").keyup(function (e) {
    if( $(this).val().length >= 3 ) {
        $("#enter").show();
        if (e.which == 13) {
            flipForm();
            $("#enter").hide();
        }
        return;
    } else {
        $("#enter").hide();
    }
});
$("#frontpasswordform").keyup(function(e) {
    if ( $(this).val().length >= 6) {
        $("#secondenter").show();
        if(e.which == 13) {
            $(this).blur();
            checkData();
        }
        return;
    } else {
        $("#secondenter").hide();
    }
});

// Main registration form
$('#signup').submit(function(e) {

    // turn off default submitting
    e.preventDefault();

    // Reg exp for avatar background filename
    var re = /([\w\d_-]*)\.?[^\\\/]*$/i;
    var userpic = $('.avatar').css('background-image').replace(/^url|[\(\)]/g, '');

    // submit form
    if (!$("input[name='ghauth']").val() && $("input[id='backloginform']").val() && $("input[id='backpasswordform']").val()) {
        var regOptions = {
            resetForm: 	false,
            data: 		{ avatar: userpic.match(re)[1]},
            datatype: 	"json",
            success: 	function(reply) {

                // If registration failed
                if (reply.state == 'fail' && reply.message) {
                    alert(reply.message);
                }
                // If registration ok
                else {
                    $('#registrationforms, .fliptext, #createaccount').fadeOut(300);
                    $('#mailform').fadeIn(500);
                    setTimeout(function(){ $("#backmailform").focus() }, 10);
                    jsonParse(reply);
                }
            }
        };
        $("#signup").ajaxSubmit(regOptions);
    }
    else alert(language.fillall);
});


// E-mail registration form
$('#mail').submit(function(e) {

    // turn off default submitting
    e.preventDefault();

    if (!$("input[name='ghauth']").val() && $("input[name='email']").val()) {	// submit form
        var mailOptions = {
            resetForm: 	false,
            datatype: 	"json",
            success: 	function(reply) {

                // Show fail message
                if (reply.state == 'fail') {
                    alert(reply.message);
                }
                // Launch app
                else {
                    setTimeout(initGreetingPage, 200);
                    setTimeout(function(){ $('#backmailform').val(''); $("#lastlogo").show(); }, 300);
                }
            }
        };
        $("#mail").ajaxSubmit(mailOptions);
    }
});

// Skip e-mail form
$("#skipmail").bind("click", function(){
    $("#lastlogo").show();
    setTimeout(initGreetingPage, 300);
});

// Userpic AJAX uploading
$('#imageInput').change(function() {
    var userpicOptions = {
        target: 		'.avatar',
        resetForm:		true,
        beforeSubmit:	beforeSubmit,
        success:		function() {
            setTimeout( function() { $(".avatar").removeClass("loadingspin"); } , 2200);
            $("#plusavatar").removeClass("infiniteavataranimation avataranimation");
        }
    };
    $('#uploadButton').ajaxSubmit(userpicOptions);
});

// Check userpic file before uploading
function beforeSubmit() {
    // Check whether browser fully supports all File API
    if (window.File && window.FileReader && window.FileList && window.Blob)
    {

        if( !$('#imageInput').val()) //check empty input filed
        {
            return false;
        }

        var fsize = $('#imageInput')[0].files[0].size; //get file size
        var ftype = $('#imageInput')[0].files[0].type; // get file type

        // Allow only valid image file types
        switch(ftype)
        {
            case 'image/png': case 'image/gif': case 'image/jpeg': case 'image/pjpeg':
            break;
            default:
                alert(language.onlyimg);
                return false
        }

        // Allowed file size is less than 1 MB (5242880)
        if(fsize>5242880)
        {
            alert(language.less5mb);
            return false
        }

        $(".avatar").toggleClass("loadingspin");
        $("#plusavatar").addClass("infiniteavataranimation");
    }
    else
    {
        // Output error to older unsupported browsers
        alert(language.oldbrowser);
        return false;
    }
    $(function() {
        $(".inputWrapper").mousedown(function() {
            var button = $(this);
            button.addClass('clicked');
            setTimeout(function() {
                button.removeClass('clicked');
            },50);
        });
    });
}

// Start processing login information
function checkData() {
    $("#piggy").toggleClass("loadingspin");
    $("#secondenter").hide();
    $("#preloader, #lastlogo").show();

    if (!$("input[name='ghauth']").val()) { // if ghost field is empty

        // AJAX login form
        var loginOptions = {
            resetForm: 	true,
            datatype: 	"json",
            error:  function() {
                $("#preloader, #enter, #secondenter").hide();
                flipForm();
                $('.frontforms').val('');
                $("#frontloginform").focus();
                alert(language.login_fail);
            },
            success: function(data) {
                jsonParse(data);
                console.log(data);
                var userAvatar = $("<img />").attr("src","assets/userpics/" + user.avatar);
                $(userAvatar).load(function() {
                    setTimeout(initGreetingPage, 500);
                });
            }
        };
        $("#auth").ajaxSubmit(loginOptions);
    }
}

// Here we go
$(window).load(function(){

    // Focus on login form
    $("#frontloginform").focus();
    setTimeout(spinThatShit, 700);

    // Check for mobile browsers
    if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
        is_mobile = true;
        FastClick.attach(document.body);
    }
    else {
        is_mobile = false;
    }
});