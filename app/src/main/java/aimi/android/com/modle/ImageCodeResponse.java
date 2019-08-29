package aimi.android.com.modle;

public class ImageCodeResponse extends BaseBean{
    private String image;

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "ImageCodeResponse{" +
                "image='" + image + '\'' +
                '}';
    }
}
