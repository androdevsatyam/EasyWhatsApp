package com.androdevsatyam.easywhatsapp;

import static com.androdevsatyam.easywhatsapp.GlobalData.*;
import static java.lang.Thread.sleep;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class WhatsAccessService extends AccessibilityService {
    String TAG = "AutoWhatsapp";
    String d = "";
    boolean groupback = false, listshow = false, back = false, h = false;
    int user_time = 200, my_time;

    public int onStartCommand(Intent intent, int i, int i2) {
        return START_STICKY;
    }

    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (GROUPSCRAPPER) {
            AccessibilityNodeInfo rootInActiveWindow;
            if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
                String a3 = event.getPackageName().toString();
                if (!a3.contains(GlobalData.APPLICATION_ID)) {
                    try {
                        if (a3.contains("com.whatsapp") && (rootInActiveWindow = getRootInActiveWindow()) != null) {
                            String str3 = this.d;
                            if (!str3.equals(rootInActiveWindow.getWindowId() + "")) {
                                this.d = rootInActiveWindow.getWindowId() + "";
                                Group(rootInActiveWindow);
                            }
                        }
                    } catch (Exception unused2) {
                        this.d = "";
                    }
                }
            } else if (AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED == event.getEventType() && listshow) {
                String a3 = event.getPackageName().toString();
                if (!a3.contains(GlobalData.APPLICATION_ID)) {
                    try {
                        if (a3.contains("com.whatsapp") && (rootInActiveWindow = getRootInActiveWindow()) != null) {
                            String str3 = this.d;
                            GroupExtract(rootInActiveWindow);
                            if (!str3.equals(rootInActiveWindow.getWindowId() + "")) {
                                this.d = rootInActiveWindow.getWindowId() + "";
                                Group(rootInActiveWindow);
                            }
                        }
                    } catch (Exception unused2) {
                        this.d = "";
                    }
                }
            }
        }
        else if (SENDWHATS) {
            Log.d(TAG, "sendWhatsapp=" + SENDWHATS);
            my_time = user_time / 4;
            Log.d(TAG, "send gap=" + user_time + " aftersend gap" + my_time);
            Log.d(TAG, "Event Type=" + event.getEventType());
            if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
                String a2 = "" + event.getPackageName();
                Log.d(TAG, "A2=" + a2);
                try {
                    if (a2.contains("com.whatsapp")) {
                        Log.d(TAG, "It's WhatsApp");
                        AccessibilityNodeInfo rootInActiveWindow2 = getRootInActiveWindow();
                        if (rootInActiveWindow2 != null) {
                            String str = this.d;
                            if (!str.equals(rootInActiveWindow2.getWindowId() + "")) {
                                this.d = rootInActiveWindow2.getWindowId() + "";
                                if (rootInActiveWindow2.getChildCount() == 2) {
                                    Log.d(TAG, "Child Count 2");
                                    if ((((Object) rootInActiveWindow2.getChild(0).getText()) + "").contains("isn't on WhatsApp")) {
                                        Log.d(TAG, "Not On whatsapp");
                                        try {
                                            send = false;
                                            fail = true;
                                            performGlobalAction(1);
                                            return;
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                            return;
                                        }
                                    }
                                }
                                if (rootInActiveWindow2.getChildCount() == 2) {
                                    Log.d(TAG, "Child Count 2");
                                    if ((((Object) rootInActiveWindow2.getChild(0).getText()) + "").contains("Couldn't look up")) {
                                        Log.d(TAG, "Couldn't look up");
                                        try {
                                            send = false;
                                            fail = true;
                                            performGlobalAction(1);
                                            return;
                                        } catch (Exception e3) {
                                            e3.printStackTrace();
                                            return;
                                        }
                                    }
                                }
                                if (rootInActiveWindow2.getChildCount() == 4) {
                                    Log.d(TAG, "Child Count 4");
                                    if ((((Object) rootInActiveWindow2.getChild(1).getText()) + "").contains("Send to")) {
                                        Log.d(TAG, "Send");
                                        try {
                                            performGlobalAction(1);
                                            return;
                                        } catch (Exception e4) {
                                            e4.printStackTrace();
                                            return;
                                        }
                                    }
                                }
                                SendButton(rootInActiveWindow2);
                            } else {
                                return;
                            }
                        }
                    } else {
                        Log.d(TAG, "Child Count 2");
                        AccessibilityNodeInfo rootInActiveWindow3 = getRootInActiveWindow();
                        if (rootInActiveWindow3 != null) {
                            String str2 = this.d;
                            if (!str2.equals(rootInActiveWindow3.getWindowId() + "")) {
                                this.d = rootInActiveWindow3.getWindowId() + "";
                                GetChild(rootInActiveWindow3);
                            } else {
                                return;
                            }
                        }
                    }
                } catch (Exception unused) {
                    this.d = "";
                }
            } else
                return;
        }
    }

    public void GetChild(AccessibilityNodeInfo rootInActiveWindow2) {
        if (rootInActiveWindow2 != null) {
            String str2 = d;
            if (!str2.equals(rootInActiveWindow2.getWindowId() + "")) {
                this.d = rootInActiveWindow2.getWindowId() + "";
                if (rootInActiveWindow2.getChildCount() == 4) {
                    if (rootInActiveWindow2.getChild(0).getText().toString().equalsIgnoreCase("Select app")) {
                        if (dual)
                            rootInActiveWindow2.getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        else
                            rootInActiveWindow2.getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                } else if (rootInActiveWindow2.getChildCount() == 2) {
                    try {
                        if (rootInActiveWindow2.getChild(1).getChild(0).getChild(1).getChild(0).getText().toString().contains("WhatsApp")) {
                            if (dual)
                                rootInActiveWindow2.getChild(1).getChild(0).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            else
                                rootInActiveWindow2.getChild(1).getChild(0).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } else if ((rootInActiveWindow2.getChild(0).getText() + "").contains("Open")) {
                            rootInActiveWindow2.getChild(dual ? 1 : 2).performAction(16);
                            return;
                        }
                        if ((rootInActiveWindow2.getChild(0).getText() + "").contains("Share")) {
                            rootInActiveWindow2.getChild(dual ? 1 : 2).performAction(16);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Error=" + e.getMessage());
                        return;
                    }
                }
            } else {
                if (rootInActiveWindow2.getChild(0).getText() != null)
                    if (rootInActiveWindow2.getChild(0).getText().toString().equalsIgnoreCase("Welcome to WhatsApp")) {
                        performGlobalAction(1);
                    } else if (rootInActiveWindow2.getChild(0).getText().toString().equalsIgnoreCase("Select app")) {
                        if (dual)
                            rootInActiveWindow2.getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        else
                            rootInActiveWindow2.getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
            }
        }
    }

    public void SendButton(AccessibilityNodeInfo nodeInfo) {
        Log.d(TAG, "send button back=" + back);
        if (first) {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                try {
                    if (nodeInfo.getChild(i) != null) {
                        Log.d(TAG, "loop count=" + i);
                        String str = ((Object) nodeInfo.getChild(i).getContentDescription()) + "";
                        if (str.equalsIgnoreCase("null"))
                            continue;
                        if (str.equals("Send")) {
                            sleep((long) (user_time + 100));
                            first = false;
                            send = true;
                            fail = false;
                            back = true;
                            Log.d(TAG, "send");
                            nodeInfo.getChild(i).performAction(16);
                            Log.d(TAG, "sent line 207 ");
                            sleep((long) (my_time + 100));
                            try {
                                performGlobalAction(1);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            return;
                        }
                    }
                } catch (Exception unused) {
                    return;
                }
            }
            goBack(nodeInfo);
        } else
            goBack(nodeInfo);
    }

    public void goBack(AccessibilityNodeInfo accessibilityNodeInfo) {
        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
            try {
                if (accessibilityNodeInfo.getChild(i) != null) {
                    Log.d(TAG, "loop count=" + i);
                    String str = ((Object) accessibilityNodeInfo.getChild(i).getContentDescription()) + "";
                    if (str.equalsIgnoreCase("null"))
                        continue;
                    if (str.equals("New chat")) {
                        Log.d(TAG, "New Chat");
                        groupback = false;
                        sleep(50);
                        try {
                            performGlobalAction(1);
                            sleep(50);
                            return;
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            return;
                        }
                    } else if (back && str.contains("Voice message,")) {
                        Log.d(TAG, "voice message");
                        sleep(50);
                        try {
                            back = false;
                            performGlobalAction(1);
                            sleep(50);
                            return;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            return;
                        }
                    } else if (str.contains("New text status")) {
                        Log.d(TAG, "New text status");
                        sleep(50);
                        try {
                            performGlobalAction(1);
                            sleep(50);
                            return;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            return;
                        }
                    } else if (str.contains("New call")) {
                        Log.d(TAG, "New call");
                        sleep(50);
                        try {
                            performGlobalAction(1);
                            sleep(50);
                            return;
                        } catch (Exception e4) {
                            e4.printStackTrace();
                            return;
                        }
                    } else if (str.equals("Camera")) {
                        Log.d(TAG, "camera");
                        if (search) {
                            Log.d(TAG, "camera back");
                            sleep(50);
                            try {
                                back = false;
                                performGlobalAction(1);
                                sleep(50);
                                return;
                            } catch (Exception e4) {
                                e4.printStackTrace();
                                return;
                            }
                        }
                    }
                }
            } catch (Exception unused) {
                return;
            }
        }

    }

    public void Group(AccessibilityNodeInfo nodeInfo) {
        int i = 0;
        System.out.println("Group groupback=" + groupback);
        if (groupback) {
            System.out.println("Group Back " + groupback);
            goBack(nodeInfo);
        } else
            for (int j = 0; j < nodeInfo.getChildCount(); j++) {
                try {
                    if (nodeInfo.getChild(j) != null) {
                        String str;
                        if (nodeInfo.getChild(j).getText() == null)
                            str = ((Object) nodeInfo.getChild(j).getContentDescription()) + "";
                        else
                            str = ((Object) nodeInfo.getChild(j).getText()) + "";
                        i = j + 1;
                        System.out.println("Group Stringvalue " + str);
                        if (str.contains("tap here for group info")) {
                            GroupNumber = "";
                            GroupName = nodeInfo.getChild(j - 1).getText().toString();
                            nodeInfo.getChild(j).getParent().performAction(16);
                        } else if (str.contains("Add group description")) {
                            nodeInfo.getChild(j).getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        } else if (str.contains("Mute notifications")) {
                            sleep(250);
                            nodeInfo.getChild(j).getParent().performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        } else if (str.contains("Search participants")) {
                            System.out.println("participants" + str);
                            listshow = true;
                            nodeInfo.getChild(j).performAction(16);
                        } else if (nodeInfo.getChild(j).getChildCount() > 0) {
                            Group(nodeInfo.getChild(j));
                        }
                    }
                } catch (Exception unused) {
                    return;
                }
            }

    }

    public void GroupExtract(AccessibilityNodeInfo nodeInfo) {
        String strnum;
        System.out.println("Group groupback=" + groupback);
        try {
            if (nodeInfo.getChildCount() == 3) {
                sleep(350);
                if (nodeInfo.getChild(0).getChildCount() > 0) {
                    for (int i = 0; i < nodeInfo.getChild(0).getChildCount(); i++) {
                        try {
                            strnum = nodeInfo.getChild(0).getChild(i).getChild(1).getText().toString();
                        } catch (Exception e) {
                            strnum = nodeInfo.getChild(0).getChild(i).getChild(0).getText().toString();
                        }
                        System.out.println("Number=>" + strnum);

                        if (GroupNumber.isEmpty())
                            GroupNumber = strnum;
                        else if (!GroupNumber.contains(strnum))
                            GroupNumber = GroupNumber + " , " + strnum;
                    }
                    if (!nodeInfo.getChild(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) {
                        nodeInfo.getChild(0).getChildCount();

                        System.out.println("Number=>" + GroupNumber);
                        groupback = true;
                        performGlobalAction(1);
                        sleep(20);
                        performGlobalAction(1);
                        sleep(30);
                        performGlobalAction(1);
                        sleep(30);
                        performGlobalAction(1);
                        listshow = false;
                        sleep(20);
                        performGlobalAction(1);
                        sleep(20);
                        goBack(nodeInfo);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("Group", "error=" + e.getLocalizedMessage());
            return;
        }
    }

    @Override
    public void onInterrupt() {
        System.out.println("Whatsapp Accessibility Service onInterrupt");
    }
}
