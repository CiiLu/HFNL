package hfnl.mod;

import javafx.scene.image.Image;
import org.jackhuang.hmcl.util.ResourceNotFoundError;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HFNLUtils {
    private static final Map<String, Image> BUILTIN_IMAGE_CACHE = new ConcurrentHashMap<>();
    //https://github.com/HMCL-dev/HMCL/blob/0e307f3b71d10eb07e34dc30502292c799423f00/HMCL/src/main/java/org/jackhuang/hmcl/ui/FXUtils.java#L793
    public static Image getBuiltinImage(String url) {
        try {
            return BUILTIN_IMAGE_CACHE.computeIfAbsent(url, Image::new);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundError("Cannot access image: " + url, e);
        }
    }

}
