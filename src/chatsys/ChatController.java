package chatsys;

import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import utils.MsgUtils;
import utils.SocketUtils;
import utils.Utils;
import chatsys.service.FriendService;
import chatsys.service.GroupService;
import chatsys.vo.Friend;
import chatsys.vo.Group;

import com.mysql.cj.util.StringUtils;

public class ChatController implements Initializable {
	private ObservableList<Group> groupData = FXCollections.observableArrayList();
	private ObservableList<Friend> friendData = FXCollections.observableArrayList();
	static Socket client;
	static User user;
	static Stage stage;
	@FXML
    private ToggleGroup opt;

	@FXML
    private ListView<Friend> friendListView;
	
	@FXML
    private ListView<Group> groupListView;

    @FXML
    private TextArea chatWin;

    @FXML
    private TextField inputTextField;
    
    @FXML
    private TabPane tabPane;
    
    public static Date lastUserChatDate;  // 最后私聊时间
    public static Date lastGroupChatDate; // 最后群聊时间
    
    Timer timer = new Timer(); 

    /**
     * 发送群聊/私聊
     * 
     * @param event
     */
    @FXML
    void sendAction(ActionEvent event) {
    	String selectedText = tabPane.getSelectionModel().getSelectedItem().getText();
    	if ("群".equals(selectedText.trim())) {
    		sendGroupChat();
    	}
    	if ("好友".equals(selectedText.trim())) {
    		sendUserChat();
    	}
    }
    
    /**
     * 发送群聊
     */
    void sendGroupChat() {
    	Group item = groupListView.getSelectionModel().getSelectedItem();
		if (item == null) {
			MsgUtils.showWarn("请选择一个群");
			return;
		}
		// 群聊@123###1###你们好--------------------------senderid,groupid,content
		String params = "群聊@" + user.userid + SocketUtils.separater + item.getId()+ SocketUtils.separater + inputTextField.getText();
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			MsgUtils.showInfo(result);
		}
		chatWin.appendText(inputTextField.getText() + "\n");
    }
    
    /**
     * 发送私聊
     */
    void sendUserChat() {
    	Friend item = friendListView.getSelectionModel().getSelectedItem();
		if (item == null) {
			MsgUtils.showWarn("请选择一个好友");
			return;
		}
		// 私聊@123###124###你好-------------------------senderid,recieverid,content
		String params = "私聊@" + user.userid + SocketUtils.separater + item.getId() + SocketUtils.separater + inputTextField.getText();
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			MsgUtils.showInfo(result);
		}
		// chatWin.appendText(inputTextField.getText() + "\n");
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
		friendListView.setItems(friendData);
		loadFriend();
		
		groupListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Group> observable, Group oldValue, Group newValue) -> {
			
		});
		
		friendListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Friend> observable, Friend oldValue, Friend newValue) -> {
			timer.cancel();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					loadUserChatNew(newValue);
				}
			}, 1000, 3000);
			loadUserChatHis(newValue);
		});
	}
	
	/**
	 * 加载群
	 */
	public void loadGroup() {
		List<Group> groupList = GroupService.getGroupList(user.userid);
		groupData.clear();
		groupData.addAll(groupList);
	}
	
	/**
	 * 加载好友
	 */
	public void loadFriend() {
		List<Friend> friendList = FriendService.getFriendList(user.userid);
		friendData.clear();
		friendData.addAll(friendList);
	}
	
	/**
	 * 加载私聊历史
	 * 
	 * @param item
	 */
	public void loadUserChatHis(Friend item) {
		// 获取私聊历史@123###124###2023-6-10 22:44:23---------------------------userid1,userid2,endtime
		String params = "获取私聊历史@" + user.userid + SocketUtils.separater + item.getId()+ SocketUtils.separater + Utils.formatDate(new Date(), Utils.DATETIME_PATTERN);
		String result = SocketUtils.request(params);
		chatWin.clear();
		if (!StringUtils.isNullOrEmpty(result)) {
			// 记录：3###123,2023-06-10 20:08:36,你好###123,2023-06-10 19:38:34,你好###123,2023-06-10 18:20:45,你好
			if (!StringUtils.isNullOrEmpty(result)) {
				System.out.println("result=" + result);
				StringBuffer buf = new StringBuffer();
				if (result.indexOf("###") > -1) {
					result = result.substring(result.indexOf("###"));
					// ###123,2023-06-10 20:08:36,你好###123,2023-06-10 19:38:34,你好###123,2023-06-10 18:20:45,你好
					List<String> contents = StringUtils.split(result, "###", true);
					for (int i = contents.size()-1; i>0; i--) {
						String str = contents.get(i);
						if (!StringUtils.isNullOrEmpty(str)) {
							String[] strs = str.split(",");
							buf.append(strs[0]).append("(").append(strs[1]).append(")\n").append(strs[2]).append("\n\n");
						}
					}
				} else {
					buf.append(result + "\n\n");
				}
				chatWin.appendText(buf.toString());
			}
		}
	}
	
	public void loadUserChatNew(Friend item) {
		// 获取私聊新记录@123###124###2000/1/1 00:00:00-----------------------------userid1,userid2,begintime
		String lastTime = "";
		if (lastUserChatDate == null) {
			lastUserChatDate = new Date();
		}
		lastTime = Utils.formatDate(lastUserChatDate, Utils.DATETIME_PATTERN);
		
		String params = "获取私聊新记录@" + user.userid + SocketUtils.separater + item.getId()+ SocketUtils.separater + lastTime;
		String result = SocketUtils.request(params);
		if (!StringUtils.isNullOrEmpty(result)) {
			// 记录：3###123,2023-06-10 20:08:36,你好###123,2023-06-10 19:38:34,你好###123,2023-06-10 18:20:45,你好---count,[{senderid,sendtime,content}]
			if (!StringUtils.isNullOrEmpty(result)) {
				// System.out.println("result=" + result);
				StringBuffer buf = new StringBuffer();
				if (result.indexOf("###") > -1) {
					result = result.substring(result.indexOf("###"));
					// ###123,2023-06-10 20:08:36,你好###123,2023-06-10 19:38:34,你好###123,2023-06-10 18:20:45,你好
					List<String> contents = StringUtils.split(result, "###", true);
					for (String str : contents) {
						if (!StringUtils.isNullOrEmpty(str)) {
							String[] strs = str.split(",");
							buf.append(strs[0]).append("(").append(strs[1]).append(")\n").append(strs[2]).append("\n\n");
							lastUserChatDate = Utils.parseDate(strs[1], Utils.DATETIME_PATTERN);
						}
					}
					chatWin.appendText(buf.toString());
				} else {
					// 暂无记录
					// buf.append(result);
				}
				// chatWin.appendText(buf.toString() + "\n");
			}
		}
	}
	
}
