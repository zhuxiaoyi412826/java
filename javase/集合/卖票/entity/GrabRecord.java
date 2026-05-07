package src.main.java.javase.集合.卖票.entity;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 抢票记录实体
 */
public class GrabRecord {
    private String userId;
    private String userName;
    private String result;
    private String recordTime;

    public GrabRecord(User user, String result) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.result = result;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.recordTime = sdf.format(new Date(user.getGrabTime()));
    }

    @Override
    public String toString() {
        return "抢票记录{" +
                "用户ID='" + userId + '\'' +
                ", 用户名='" + userName + '\'' +
                ", 结果='" + result + '\'' +
                ", 时间='" + recordTime + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public String getResult() {
        return result;
    }
}
