package com.bmop.demo.manager;

import com.bmop.demo.data.FriendsData;
import com.bmop.demo.data.SpeechData;
import com.bmop.demo.data.UserData;
import com.bmop.demo.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
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
    private List<UserData> friends = new ArrayList<UserData>(); // 好友列表
    private List<SpeechData> userSpeeches = new ArrayList<SpeechData>(); // 用户说说列表
    private List<SpeechData> friendsSpeeches = new ArrayList<SpeechData>(); // 好友说说列表

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

                    BmobQuery<SpeechData> speechQuery = new BmobQuery<SpeechData>();
                    speechQuery.addWhereEqualTo("author", currentUser);
                    speechQuery.findObjects(new FindListener<SpeechData>() {
                        @Override
                        public void done(List<SpeechData> list, BmobException e) {
                            Logger.e("user speeches: " + list);
                        }
                    });
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

    public List<UserData> getFriends() {
        return friends;
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
                        /*String bql = "select * from UserData where related friends to pointer('FriendsData', " + "'" + list.get(0).getObjectId() + "'" + ")";
                        Logger.e("bql: " + bql);
                        new BmobQuery<UserData>().doSQLQuery(bql, new SQLQueryListener<UserData>() {
                            @Override
                            public void done(BmobQueryResult<UserData> queryResult, BmobException e) {
                                Logger.e("--" + e);
                                queryResult.getResults();
                            }
                        });*/

                        BmobQuery<UserData> userQuery = new BmobQuery<UserData>();
                        userQuery.addWhereRelatedTo("friends", new BmobPointer(list.get(0)));
                        userQuery.findObjects(new FindListener<UserData>() {
                            @Override
                            public void done(List<UserData> list, BmobException e) {
                                if (list.size() > 0) {
                                    friends.clear();
                                    friends.addAll(list);
                                    // 查询所有好友的说说数据
                                    List<BmobQuery<SpeechData>> queries = new ArrayList<BmobQuery<SpeechData>>();
                                    for (UserData userData : list) {
                                        BmobQuery<SpeechData> speechQuery = new BmobQuery<SpeechData>();
                                        speechQuery.addWhereEqualTo("author", userData);
                                        queries.add(speechQuery);
                                    }

                                    BmobQuery<SpeechData> fQuery = new BmobQuery<SpeechData>();
                                    fQuery.order("-updatedAt"); // 按更新时间排序
                                    fQuery.include("author"); // 返回的数据包含用户信息
                                    fQuery.or(queries);
                                    fQuery.findObjects(new FindListener<SpeechData>() {
                                        @Override
                                        public void done(List<SpeechData> list, BmobException e) {
                                            friendsSpeeches.clear();
                                            friendsSpeeches.addAll(list);
                                        }
                                    });
                                }
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
