package dandandateam.dandanda.Items;

import java.io.Serializable;

public class ProfileItem implements Serializable{
    public  String imageSrc; //图片的位置
    public  String label;  //标签的内容
    public  String content;       //标签对应的数字

    public ProfileItem(String imageSrc, String label, String content) {
        this.imageSrc = imageSrc;
        this.label = label;
        this.content = content;
    }
}
