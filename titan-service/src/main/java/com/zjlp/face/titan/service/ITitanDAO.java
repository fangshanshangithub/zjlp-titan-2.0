package com.zjlp.face.titan.service;

import java.util.List;
import java.util.Map;

public interface ITitanDAO {

    /**
     * 增加一个好友关系
     * @param userId
     * @param friendUserId
     */
    void addRelation(String userId, String friendUserId);

    /**
     * 删除一个好友关系
     * @param userId
     * @param friendUserId
     */
    void deleteRelation(String userId, String friendUserId);

    /**
     * 查询一度和二度好友
     * @param userId
     * @param friends
     * @return
     */
    Map<String, Integer> getFriendsLevel(String userId, List<String> friends);

    /**
     * 查询共同好友数
     * @param userId
     * @param friends
     * @return
     */
    Map<Object, Long> getComFriendsNum(String userId, List<String> friends);

}
