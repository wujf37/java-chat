package chatsys.service;

import java.util.ArrayList;
import java.util.List;

import utils.SocketUtils;
import chatsys.vo.Group;

import com.mysql.cj.util.StringUtils;

public class GroupService {
	public static List<Group> getGroupList(int userid) {
		List<Group> groupList = new ArrayList<>();
		// 获取群聊列表@123-----------------userid
		String params = "获取群聊列表@" + userid;
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			result = result.substring(result.indexOf("###"));
			// result+="###"+rs.getString("userid")+','+rs.getString("username");
			List<String> friends = StringUtils.split(result, "###", true);
			for (String str : friends) {
				if (!StringUtils.isNullOrEmpty(str)) {
					String[] strs = str.split(",");
					Group us = new Group();
					us.setId(Integer.valueOf(strs[0]));
					us.setName(strs[1]);
					groupList.add(us);
				}
			}
		}
		return groupList;
	}
}
