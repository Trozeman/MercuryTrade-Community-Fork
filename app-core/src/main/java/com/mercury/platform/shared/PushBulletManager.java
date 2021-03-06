package com.mercury.platform.shared;

import com.github.sheigutn.pushbullet.Pushbullet;
import com.github.sheigutn.pushbullet.items.chat.Chat;
import com.github.sheigutn.pushbullet.items.device.Device;
import com.github.sheigutn.pushbullet.items.push.sendable.SendablePush;
import com.github.sheigutn.pushbullet.items.push.sendable.defaults.SendableFilePush;
import com.github.sheigutn.pushbullet.items.push.sendable.defaults.SendableNotePush;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.TaskBarDescriptor;
import com.mercury.platform.shared.config.descriptor.VulkanDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.sun.jna.Native;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import org.apache.commons.lang3.SystemUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PushBulletManager {
    public static PushBulletManager INSTANCE = PushBulletManagerHolder.HOLDER_INSTANCE;
    protected TaskBarDescriptor config;
    private String pushbulletAccessToken;
    private Pushbullet pushbullet;

    private PushBulletManager() {
        reloadAccessToken();
    }

    public void reloadAccessToken() {
        pushbulletAccessToken = Configuration.get().applicationConfiguration().get().getPushbulletAccessToken();
        config = Configuration.get().taskBarConfiguration().get();
        if (pushbulletAccessToken != null && !pushbulletAccessToken.isEmpty()) {
            pushbulletAccessToken = pushbulletAccessToken.replace(" ", "");
            pushbullet = new Pushbullet(pushbulletAccessToken);
        }
    }

    public void sendPush(String content) {
        config = Configuration.get().taskBarConfiguration().get();
        if (pushbullet != null && config.isPushbulletOn()) {
            pushbullet.push(new SendableNotePush("MercuryTrade", content));
        }
    }

    public void sendPush(String content, String nickName) {
        config = Configuration.get().taskBarConfiguration().get();
        if (pushbullet != null && config.isPushbulletOn()) {
            pushbullet.push(new SendableNotePush("MercuryTrade - " + nickName, content));
        }
    }

    public void testPush() {
        if (pushbullet != null && config.isPushbulletOn()) {
            pushbullet.push(new SendableNotePush("MercuryTrade" , "Test notification"));
        }
    }
    private static class PushBulletManagerHolder {
        static final PushBulletManager HOLDER_INSTANCE = new PushBulletManager();
    }
}
