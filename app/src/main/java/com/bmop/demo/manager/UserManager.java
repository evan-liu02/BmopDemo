package com.bmop.demo.manager;

import com.bmop.demo.data.FriendsData;
import com.bmop.demo.data.UserData;
import com.bmop.demo.utils.Logger;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserManager {
    private static UserManager self;
    private UserData currentUser;

    public static UserManager getInstance() {
        if (self == null) {
            synchronized (UserManager.class) {
                if (self == null) {
                    self = new UserManager();
                }
            }
        }
        return self;
    }

    private UserManager() {

    }

    public void register(String phone, String password) {
        UserData userData = new UserData();
        userData.setPhone(phone);
        userData.setPassword(password);
        userData.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                Logger.e(objectId);
                if (e == null) {

                } else {

                }
            }
        });
    }

    public void login(String phone, String password) {
        BmobQuery<UserData> userQuery = new BmobQuery<UserData>();
        userQuery.addWhereEqualTo("phone", phone);
//        userQuery.setLimit(1);
        userQuery.findObjects(new FindListener<UserData>() {
            @Override
            public void done(List<UserData> list, BmobException e) {
                if (e == null) {
                    Logger.e(list.get(0).getObjectId() + "");
                    currentUser = list.get(0);
                    /*SpeechData speechData = new SpeechData();
                    speechData.setContent("说说内容");
                    speechData.setAuthor(list.get(0));
                    speechData.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Log.e("Test", s);
                        }
                    });*/
                }
            }
        });
    }

    private UserData getCurrentUser() {
        return currentUser;
    }

    public void addFriend(final UserData user) {
        if (currentUser == null) {
            Logger.e("尚未登录");
            return;
        }

        if (user == null) {
            Logger.e("user is null");
            return;
        }

        // 查询好友关系
        BmobQuery<FriendsData> query = new BmobQuery<FriendsData>();
        query.setLimit(1);
        query.addWhereEqualTo("user", currentUser);
        query.findObjects(new FindListener<FriendsData>() {
            @Override
            public void done(List<FriendsData> list, BmobException e) {
                Logger.e("add friend: " + e + ", size = " + list.size());
                if (e == null) {
                    BmobRelation friends = new BmobRelation();
                    friends.add(user);

                    if (list.size() == 0) {
                        // 添加一条好友关系
                        final FriendsData friendsData = new FriendsData();
                        friendsData.setUser(currentUser);

                        friendsData.setFriends(friends);
                        friendsData.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                Logger.e("add: " + e);
                            }
                        });
                    } else {
                        // 更新好友数据
                        final FriendsData friendsData = new FriendsData();
                        friendsData.setObjectId(list.get(0).getObjectId());

                        friendsData.setFriends(friends);
                        friendsData.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                Logger.e("update: " + e);
                            }
                        });
                    }
                } else {
                    Logger.e("获取好友关系失败");
                }
            }
        });
    }

    public void queryFriends() {
        if (currentUser == null) {
            Logger.e("尚未登录");
            return;
        }
        // 查询好友关系
        BmobQuery<FriendsData> query = new BmobQuery<FriendsData>();
        query.setLimit(1);
        query.addWhereEqualTo("user", currentUser);
        query.findObjects(new FindListener<FriendsData>() {
            @Override
            public void done(List<FriendsData> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        String bql = "select * from UserData where related friends to pointer('FriendsData', " + "'" + list.get(0).getObjectId() + "'" + ")";
                        Logger.e("bql: " + bql);
                        new BmobQuery<UserData>().doSQLQuery(bql, new SQLQueryListener<UserData>() {
                            @Override
                            public void done(BmobQueryResult<UserData> queryResult, BmobException e) {
                                Logger.e("--" + e);
                                queryResult.getResults();
                            }
                        });
                    } else {
                        Logger.e("暂无好友");
                    }
                } else {
                    Logger.e("获取好友列表失败");
                }
            }
        });
    }
}
