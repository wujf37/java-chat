package chatsys;

import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
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
import chatsys.service.FriendService;
import chatsys.vo.Friend;

import com.mysql.cj.util.StringUtils;

public class FriendController implements Initializable {
	private ObservableList<Friend> friendData = FXCollections.observableArrayList();
	static Socket client;
	static User user;
	static Stage stage;
	@FXML
	private ListView<Friend> friendListView;

	@FXML
	private TextField inputTextField;

	/**
	 * 添加好友
	 * 
	 * @param event
	 */
	@FXML
	void addFriendAction(ActionEvent event) {
		String text = inputTextField.getText();
		if (StringUtils.isNullOrEmpty(text)) {
			MsgUtils.showWarn("请填写用户id");
			return;
		}
		int userid = 0;
		if (Utils.isNumeric(text)) {
			userid = Integer.valueOf(text);
		} else {
			MsgUtils.showWarn("请填写正确的用户id");
			return;
		}
		// 加好友@123###124
		String params = "加好友@" + user.userid + SocketUtils.separater + userid;
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			MsgUtils.showInfo(result);
		}
		loadFriend();
	}

	/**
	 * 删除好友
	 * 
	 * @param event
	 */
	@FXML
	void deleteFriendAction(ActionEvent event) {
		Friend item = friendListView.getSelectionModel().getSelectedItem();
		if (item == null) {
			MsgUtils.showWarn("请选择一个好友");
			return;
		}
		// 删好友@123###124
		String params = "删好友@" + user.userid + SocketUtils.separater + item.getId();
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			MsgUtils.showInfo(result);
		}
		loadFriend();
	}
    
	public static void setObject(Socket c,User u,Stage s) {
    	client=c;
    	user=u;
    	stage=s;
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		friendListView.setItems(friendData);
		loadFriend();
	}
	
	public void loadFriend() {
		List<Friend> friendList = FriendService.getFriendList(user.userid);
		friendData.clear();
		friendData.addAll(friendList);
	}
}
