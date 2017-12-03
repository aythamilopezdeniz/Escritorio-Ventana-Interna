
import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.core.image.ConvertImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Lienzo extends JPanel{
    
    private File imageFile;
    private BufferedImage image;
    
    public Lienzo() {
        this.image = null;
        this.imageFile = null;
        super.setBackground(Color.WHITE); 
    }    

    public File getImageFile() {
        return this.imageFile;
    }
    
    public BufferedImage getImage() {
        return image;
    }    
    
    public void setImage (File imageFile) {
        try {
            this.imageFile = imageFile;
            this.image = ImageIO.read(imageFile);
            this.setPreferredSize(
                new Dimension(this.image.getWidth(), this.image.getHeight()));
            revalidate();
        } catch (IOException ex) {
            System.out.println("Error Leyendo Imagen");
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }   

    public void umbralizar(int umbral) {
        // convierte la imagen en color BufferedImage en formato de la librería BoofCV
        Planar<GrayU8> imagenColor = ConvertBufferedImage.convertFromPlanar(
                this.image,null, true, GrayU8.class);
        // crea dos imágenes en niveles de grises
        GrayU8 imagenGris = new GrayU8(this.image.getWidth(), this.image.getHeight());
        GrayU8 imagenUmbralizada = new GrayU8(this.image.getWidth(), this.image.getHeight());
        // Convierte a niveles de gris la imagen de entrada
        ConvertImage.average(imagenColor,imagenGris);
        // umbraliza la imagen:
        // ‐ píxeles con nivel de gris > umbral se ponen a 1
        // ‐ píxeles con nivel de gris <= umbra se ponen a 0
        GThresholdImageOps.threshold(imagenGris, imagenUmbralizada, umbral, false);
        // se devuelve la imagen umbralizada en formato BufferedImage
        this.image = VisualizeBinaryData.renderBinary(imagenUmbralizada, false, null);
    }
    
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, null);
    }
    
}
