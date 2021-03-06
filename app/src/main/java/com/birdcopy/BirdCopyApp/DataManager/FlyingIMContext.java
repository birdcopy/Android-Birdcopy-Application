package com.birdcopy.BirdCopyApp.DataManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.birdcopy.BirdCopyApp.Http.FlyingHttpTool;
import com.birdcopy.BirdCopyApp.MyApplication;
import com.birdcopy.BirdCopyApp.ShareDefine;
import com.birdcopy.BirdCopyApp.DataManager.ActiveDAO.BE_RongUser;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by vincent sung on 2015/7/9.
 */
public class FlyingIMContext {

    private static FlyingIMContext mDemoContext;
    public Context mContext;
    private HashMap<String, Group> groupMap;
    private FlyingRongUserDAO mRongUserDAO = new FlyingRongUserDAO();
    private ArrayList<UserInfo> mFriendInfos;


    public static FlyingIMContext getInstance() {

        if (mDemoContext == null) {
            mDemoContext = new FlyingIMContext();
        }

        return mDemoContext;
    }

    private FlyingIMContext() {

    }

    private FlyingIMContext(Context context) {
        mContext = context;
        mDemoContext = this;
    }

    public static void init(Context context) {
        mDemoContext = new FlyingIMContext(context);
    }

    public void setGroupMap(HashMap<String, Group> groupMap) {
        this.groupMap = groupMap;
    }

    public HashMap<String, Group> getGroupMap() {
        return groupMap;
    }

    public ArrayList<UserInfo> getUserList()
    {

        ArrayList<UserInfo> result =new ArrayList<UserInfo>();

        for(BE_RongUser rongUser:mRongUserDAO.loadAllData())
        {
            UserInfo userInfo= new UserInfo(rongUser.getUserid(),rongUser.getName(),Uri.parse(rongUser.getPortraitUri()));

            result.add(userInfo);
        }

        return result;
    }
    /*
    public void setUserInfos(ArrayList<UserInfo> userInfos) {
        mUserInfos = userInfos;
    }
    */

    /**
     * 临时存放用户数据
     *
     * @param userInfos
     */
    public void setFriends(ArrayList<UserInfo> userInfos) {

        this.mFriendInfos = userInfos;
    }

    public ArrayList<UserInfo> getFriends() {
        return mFriendInfos;
    }

    /*
    public DemoApi getDemoApi() {
        return mDemoApi;
    }
    */


    /**
     * 增加用户信息
     *
     * @param userInfo
     * @return
     */
    public void addOrReplaceRongUserInfo(UserInfo userInfo)
    {
        BE_RongUser user = new BE_RongUser();
        user.setUserid(userInfo.getUserId());
        user.setName(userInfo.getName());
        user.setPortraitUri(userInfo.getPortraitUri().toString());

        mRongUserDAO.saveRongUser(user);

        RongIM.getInstance().refreshUserInfoCache(userInfo);
    }


    /**
     * 获取用户信息
     *
     * @param rongID
     * @return
     */
    public UserInfo getUserInfoByRongId(final String rongID)
    {
        BE_RongUser rongUser =mRongUserDAO.selectWithUserID(rongID);

        if(rongUser==null)
        {
            FlyingHttpTool.getUserInfoByRongID(rongID);

            return null;
        }
        else
        {
            return new UserInfo(rongUser.getUserid(), rongUser.getName(), Uri.parse(rongUser.getPortraitUri()));
        }
    }

    /**
     * 通过userid 获得username
     *
     * @param userId
     * @return
     */
    public String getUserNameByUserId(String userId) {

        UserInfo userInfo =getUserInfoByRongId(userId);

        if (userInfo!=null)
        {

            return userInfo.getName();
        }
        else
        {

            return null;
        }
    }

    /**
     * 获取用户信息列表
     *
     * @param userIds
     * @return
     */
    public List<UserInfo> getUserInfoByIds(String[] userIds) {

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();

        if (userIds != null && userIds.length > 0) {
            for (String userId : userIds)
            {
                userInfoList.add(getUserInfoByRongId(userId));
            }
        }
        return userInfoList;
    }

    /**
     * 根据userids获得好友列表
     *
     * @return
     */
    public ArrayList<UserInfo> getUserInfoList(List list) {

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();


        return (ArrayList) userInfoList;
    }

    /**
     * 通过groupid 获得groupname
     *
     * @param groupid
     * @return
     */
    public String getGroupNameById(String groupid) {
        Group groupReturn = null;
        if (!TextUtils.isEmpty(groupid) && groupMap != null) {

            if (groupMap.containsKey(groupid)) {
                groupReturn = groupMap.get(groupid);
            }else
                return null;

        }
        return groupReturn.getName();
    }
}
