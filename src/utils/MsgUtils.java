package utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class MsgUtils {
	public static Optional<ButtonType> showInfo(String msg) {
		return show(msg, Alert.AlertType.INFORMATION);
	}
	public static Optional<ButtonType> showWarn(String msg) {
		return show(msg, Alert.AlertType.WARNING);
	}
	public static boolean showConfirm(String msg) {
		Optional<ButtonType> buttonType = show(msg, Alert.AlertType.CONFIRMATION);
        if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) { // 确定
            return true;
        } else { // 取消
            return false;
        }
	}
	public static Optional<ButtonType> showError(String msg) {
		return show(msg, Alert.AlertType.ERROR);
	}
	private static Optional<ButtonType> show(String msg, Alert.AlertType type) {
		Alert alert = new Alert(type);
		alert.setContentText(msg);
		alert.setHeaderText(msg);
		Optional<ButtonType> buttonType = alert.showAndWait();
		return buttonType;
	}
}
