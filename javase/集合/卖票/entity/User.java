package src.main.java.javase.集合.卖票.entity;


public class User {
    private String userId;
    private String userName;
    private boolean isSuccess;
    private long grabTime;

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.isSuccess = false;
        this.grabTime = System.currentTimeMillis();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public long getGrabTime() {
        return grabTime;
    }
}
