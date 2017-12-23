package FileType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;


public class fileType {
	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();
	
	public static void getAllFileType(){    
		FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPEG (jpg)    
	    FILE_TYPE_MAP.put("png", "89504E47");  //PNG (png)    
	    FILE_TYPE_MAP.put("gif", "47494638");  //GIF (gif)    
	    FILE_TYPE_MAP.put("tif", "49492A00");  //TIFF (tif)    
	    FILE_TYPE_MAP.put("bmp", "424D"); //Windows Bitmap (bmp)    
	    FILE_TYPE_MAP.put("dwg", "41433130"); //CAD (dwg)    
	    FILE_TYPE_MAP.put("html", "68746D6C3E");  //HTML (html)    
	    FILE_TYPE_MAP.put("rtf", "7B5C727466");  //Rich Text Format (rtf)    
	    FILE_TYPE_MAP.put("xml", "3C3F786D6C");    
	    FILE_TYPE_MAP.put("zip", "504B0304");    
	    FILE_TYPE_MAP.put("rar", "52617221");    
	    FILE_TYPE_MAP.put("psd", "38425053");  //Photoshop (psd)    
	    FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A");  //Email [thorough only] (eml)    
	    FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F");  //Outlook Express (dbx)    
	    FILE_TYPE_MAP.put("pst", "2142444E");  //Outlook (pst)    
	    FILE_TYPE_MAP.put("xls", "D0CF11E0");  //MS Word    
	    FILE_TYPE_MAP.put("doc", "D0CF11E0");  //MS Excel 注意：word 和 excel的文件头一样    
	    FILE_TYPE_MAP.put("mdb", "5374616E64617264204A");  //MS Access (mdb)    
	    FILE_TYPE_MAP.put("wpd", "FF575043"); //WordPerfect (wpd)     
	    FILE_TYPE_MAP.put("eps", "252150532D41646F6265");    
	    FILE_TYPE_MAP.put("ps", "252150532D41646F6265");    
	    FILE_TYPE_MAP.put("pdf", "255044462D312E");  //Adobe Acrobat (pdf)    
	    FILE_TYPE_MAP.put("qdf", "AC9EBD8F");  //Quicken (qdf)    
	    FILE_TYPE_MAP.put("pwl", "E3828596");  //Windows Password (pwl)    
	    FILE_TYPE_MAP.put("wav", "57415645");  //Wave (wav)    
	    FILE_TYPE_MAP.put("avi", "41564920");    
	    FILE_TYPE_MAP.put("ram", "2E7261FD");  //Real Audio (ram)    
	    FILE_TYPE_MAP.put("rm", "2E524D46");  //Real Media (rm)    
	    FILE_TYPE_MAP.put("mpg", "000001BA");  //    
	    FILE_TYPE_MAP.put("mov", "6D6F6F76");  //Quicktime (mov)    
	    FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); //Windows Media (asf)    
	    FILE_TYPE_MAP.put("mid", "4D546864");  //MIDI (mid)    
	 } 
	
	public static final boolean isImage(File file){  
        boolean flag = false;  
        try  
        {  
            BufferedImage bufreader = ImageIO.read(file);  
            int width = bufreader.getWidth();  
            int height = bufreader.getHeight();  
            if(width==0 || height==0){  
                flag = false;  
            }else {  
                flag = true;  
            }  
        }  
        catch (IOException e)  
        {  
            flag = false;  
        }catch (Exception e) {  
            flag = false;  
        }  
        return flag;  
    }  
	
    public final static String getImageFileType(File f)    
    {    
        if (isImage(f))  
        {  
            try  
            {  
                ImageInputStream iis = ImageIO.createImageInputStream(f);  
                Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);  
                if (!iter.hasNext())  
                {  
                    return null;  
                }  
                ImageReader reader = iter.next();  
                iis.close();  
                return reader.getFormatName();  
            }  
            catch (IOException e)  
            {  
                return null;  
            }  
            catch (Exception e)  
            {  
                return null;  
            }  
        }  
        return null;  
    }    

}
