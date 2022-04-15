package ch.skyfy.samples.ui.utils;


import ch.skyfy.samples.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
public class FXMLUtils {
    public static <R> void loadFXML(R view) {
        loadFXML("ui/fxml/" + view.getClass().getSimpleName() + ".fxml", view);
    }

    public static <R> void loadFXML(String path, R view) {
        try {
            new FXMLLoader(Main.class.getResource(path)) {{
                setController(view);
                setRoot(view);
            }}.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <R extends Parent> R buildView(Class<R> viewClass, Class<?>[] parameterTypes, Object... params) {
        var loader = new FXMLLoader(Main.class.getResource("ui/fxml/" + viewClass.getSimpleName() + ".fxml"));
        loader.setControllerFactory(param -> {
            try {
                return param.getDeclaredConstructor(parameterTypes).newInstance(params);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        });
        var view = loader.getControllerFactory().call(viewClass);
        loader.setRoot(view);
        loader.setController(view);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (R) view;
    }

    public static <R extends Parent> R buildView(Class<R> viewClass) {
        var loader = new FXMLLoader(Main.class.getResource("ui/fxml/" + viewClass.getSimpleName() + ".fxml"));
        try {
            var view = viewClass.getDeclaredConstructor().newInstance();
            loader.setRoot(view);
            loader.setController(view);
            loader.load();
            return view;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
