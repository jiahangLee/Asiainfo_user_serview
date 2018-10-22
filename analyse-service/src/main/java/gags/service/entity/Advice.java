package gags.service.entity;

import gags.service.util.DateUtil;

import java.util.Date;

/**
 * @author <a href="mailto:wanggy6@asiainfo.com">wanggy6</a>
 * @version 1.0
 */
public class Advice {
    public String advice_user;
    public String advice_message;
    public Date create_time;

    public String getAdvice_user() {
        return advice_user;
    }

    public void setAdvice_user(String advice_user) {
        this.advice_user = advice_user;
    }

    public String getAdvice_message() {
        return advice_message;
    }

    public void setAdvice_message(String advice_message) {
        this.advice_message = advice_message;
    }

    public String getCreate_time() {
        return DateUtil.formatDate(create_time,DateUtil.DATE_TYPE_YYYY_MM_DD_HHMMSS);
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

}
