package chatsys;

import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.MsgUtils;
import utils.SocketUtils;
import utils.Utils;
import chatsys.service.GroupService;
import chatsys.vo.Group;

import com.mysql.cj.util.StringUtils;

public class GroupController implements Initializable {
	private ObservableList<Group> groupData = FXCollections.observableArrayList();
	static Socket client;
	static User user;
	static Stage stage;
	@FXML
    private ListView<Group> groupListView;

    @FXML
    private TextField inputTextField;
    
    @FXML
    private TextField newGroupNameTextField;

    /**
     * 退出群聊
     * 
     * @param event
     */
    @FXML
    void exitGroupAction(ActionEvent event) {
    	Group item = groupListView.getSelectionModel().getSelectedItem();
		if (item == null) {
			MsgUtils.showWarn("请选择一个群");
			return;
		}
		// 退群@123###4-------------------userid,groupid
		String params = "退群@" + user.userid + SocketUtils.separater + item.getId();
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			MsgUtils.showInfo(result);
		}
		loadGroup();
    }

    /**
     * 邀请入群
     * 
     * @param event
     */
    @FXML
    void inviteInGroupAction(ActionEvent event) {
    	String friendIdStr = inputTextField.getText();
		if (StringUtils.isNullOrEmpty(friendIdStr)) {
			MsgUtils.showWarn("请填写好友id");
			return;
		}
		int friendId = 0;
		if (Utils.isNumeric(friendIdStr)) {
			friendId = Integer.valueOf(friendIdStr);
		} else {
			MsgUtils.showWarn("请填写正确的好友id");
			return;
		}
		
		Group item = groupListView.getSelectionModel().getSelectedItem();
		if (item == null) {
			MsgUtils.showWarn("请选择一个群");
			return;
		}
		// 加群@123###4-------------------userid,groupid
		String params = "加群@" + friendId + SocketUtils.separater + item.getId();
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			MsgUtils.showInfo(result);
		}
		loadGroup();
    }

    /**
     * 创建新群
     * 
     * @param event
     */
    @FXML
    void createGroupAction(ActionEvent event) {
    	String text = newGroupNameTextField.getText();
		if (StringUtils.isNullOrEmpty(text)) {
			MsgUtils.showWarn("请填写新的群名称");
			return;
		}
		// 建群@123###青龙学习小组
		String params = "建群@" + user.userid + SocketUtils.separater + text;
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			MsgUtils.showInfo(result);
		}
		loadGroup();
    }
    
    public static void setObject(Socket c,User u,Stage s) {
    	client=c;
    	user=u;
    	stage=s;
    }
	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	groupListView.setItems(groupData);
		loadGroup();
	}
    
    public void loadGroup() {
		List<Group> groupList = GroupService.getGroupList(user.userid);
		groupData.clear();
		groupData.addAll(groupList);
	}
}
