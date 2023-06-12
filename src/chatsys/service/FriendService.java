package chatsys.service;

import java.util.ArrayList;
import java.util.List;

import utils.SocketUtils;

import com.mysql.cj.util.StringUtils;

import chatsys.vo.Friend;

public class FriendService {
	public static List<Friend> getFriendList(int userid) {
		List<Friend> friendList = new ArrayList<>();
		String params = "获取好友列表@" + userid;
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			result = result.substring(result.indexOf("###"));
			// result+="###"+rs.getString("userid")+','+rs.getString("username");
			List<String> friends = StringUtils.split(result, "###", true);
			for (String str : friends) {
				if (!StringUtils.isNullOrEmpty(str)) {
					String[] strs = str.split(",");
					Friend us = new Friend();
					us.setId(Integer.valueOf(strs[0]));
					us.setName(strs[1]);
					friendList.add(us);
				}
			}
		}
		return friendList;
	}
}
