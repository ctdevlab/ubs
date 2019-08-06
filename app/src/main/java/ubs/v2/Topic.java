package ubs.v2;

public class Topic {
    String topic;
    String uid;
    String key;

    public Topic() {
        // must have
    }

    public Topic(String topic, String uid, String key) {
        this.topic = topic;
        this.uid = uid;
        this.key = key;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return topic;
    }
}
