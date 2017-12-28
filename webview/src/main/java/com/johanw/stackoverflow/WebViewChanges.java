package com.johanw.stackoverflow;

import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGWebView;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class WebViewChanges {
//    public static String MY_WEBVIEW_CLASSNAME = WebView.class.getName();

    public WebView newWebView() {
//        try {
            createSubclass();
            return new WebView();
/*
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
*/
    }

    // https://www.ibm.com/developerworks/library/j-dyn0916/index.html
    // Need to change the method, not override!
    boolean created = false;
    private void createSubclass() {
        if (created) return;
        created = true;
        try
        {
            String methodName = "handleStagePulse";

            // get the super class
            CtClass webViewClass = ClassPool.getDefault().get("javafx.scene.web.WebView");

            // get the method you want to override
            CtMethod handleStagePulseMethod = webViewClass.getDeclaredMethod(methodName);

            // Rename the previous handleStagePulse method
            String newName = methodName+"Old";
            handleStagePulseMethod.setName(newName);

            //  mnew.setBody(body.toString());
            CtMethod newMethod = CtNewMethod.copy(handleStagePulseMethod, methodName, webViewClass, null);
            String body = "{" +
                    "  " + Scene.class.getName() + ".impl_setAllowPGAccess(true);\n" +
                    "  " + "final " + NGWebView.class.getName() + " peer = impl_getPeer();\n" +
                    "  " + "peer.update(); // creates new render queues\n" +
//                    "  " + "if (page.isRepaintPending()) {\n" +
                    "  " + "   impl_markDirty(" + DirtyBits.class.getName() + ".WEBVIEW_VIEW);\n" +
//                    "  " + "}\n" +
                    "  " + Scene.class.getName() + ".impl_setAllowPGAccess(false);\n" +
                    "}\n";
            System.out.println(body);
            newMethod.setBody(body);
            webViewClass.addMethod(newMethod);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}