//package javaimgedit;

import javax.swing.*;
import java.awt.*;
import java.awt.image.Kernel;
import java.awt.image.ConvolveOp;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

//Implement a notification to let user know of imports/exports

public class CustomImageEditor extends JFrame {
    private BufferedImage image, originalImage;
    private JLabel imageLabel;
	private JSlider sharpenSlider, blurSlider, pixelSlider, scramSlider, ambOSlider, brightSlider;
	private String imagePath; //store the original file path
	private JPanel settingsPanel;
    private boolean settingsVisible = true; 
	
    public CustomImageEditor() {
        setTitle("Java Image Editor v1.0");
        setSize(1600, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu fxMenu = new JMenu("Special Effects");
		JMenu transformMenu = new JMenu("Transform");
		
		final JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(26, 2));
		settingsPanel.add(new JLabel("Effect Settings:"));
		settingsPanel.setBackground(Color.LIGHT_GRAY);

		//editing sliders
        sharpenSlider = new JSlider(3, 10, 5);
		sharpenSlider.addChangeListener(new ChangeListener() { //update tool tip values
            @Override
            public void stateChanged(ChangeEvent e) {
				sharpenSlider.setToolTipText("Sharpening intensity " + sharpenSlider.getValue());
				revalidate();
				repaint();
            }
        });
        blurSlider = new JSlider(1, 3, 1);
		blurSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
				blurSlider.setToolTipText("Blur strength " + blurSlider.getValue());
            }
        });
        brightSlider = new JSlider(-100, 100, 10);
		brightSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
				brightSlider.setToolTipText("Brightness value " + brightSlider.getValue());
            }
        });
        pixelSlider = new JSlider(2, 50, 10);
		pixelSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
				pixelSlider.setToolTipText("Pixel size " + pixelSlider.getValue());
            }
        });
        scramSlider = new JSlider(2, 12, 5);
		scramSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
				scramSlider.setToolTipText("Scramble size " + scramSlider.getValue());
            }
        });
        ambOSlider = new JSlider(20, 60, 30);
		ambOSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
				ambOSlider.setToolTipText("Shade value " + ambOSlider.getValue());
            }
        });

        settingsPanel.add(new JLabel("Sharpen Intensity: "));
        settingsPanel.add(sharpenSlider);
        settingsPanel.add(new JLabel("Blur Strength:"));
        settingsPanel.add(blurSlider);
		settingsPanel.add(new JLabel("Brightness:"));
        settingsPanel.add(brightSlider);
		settingsPanel.add(new JLabel("Pixel Size:"));
        settingsPanel.add(pixelSlider);
		settingsPanel.add(new JLabel("Scramble Amount:"));
        settingsPanel.add(scramSlider);
		settingsPanel.add(new JLabel("Ambient Occlusion Strength:"));
        settingsPanel.add(ambOSlider);

        add(settingsPanel, BorderLayout.EAST);

        JMenuItem loadImageItem = new JMenuItem("Load Image");
		loadImageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        loadImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadImage();
            }
        });

        JMenuItem saveImageItem = new JMenuItem("Save Image");
		saveImageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });

		JMenuItem reloadImageItem = new JMenuItem("Reload Image");
		reloadImageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        reloadImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadImage();
            }
        });
		
        JMenuItem toggleSettingsItem = new JMenuItem("Hide Settings Panel");
		toggleSettingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        toggleSettingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				if (settingsVisible = true) {
					settingsVisible = false;
					settingsPanel.setVisible(false);
				}
            }
        });
		
        JMenuItem toggleSettingsItem2 = new JMenuItem("Show Settings Panel");
		toggleSettingsItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
        toggleSettingsItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				settingsVisible = true;
				settingsPanel.setVisible(true);
            }
        });

		//----------------------Transform----------------------
		JMenuItem rotate90Item = new JMenuItem("Rotate 90°");
		rotate90Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateImage(90);
            }
        });

		JMenuItem rotate180Item = new JMenuItem("Rotate 180°");
		rotate180Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateImage(180);
            }
        });

		JMenuItem rotate270Item = new JMenuItem("Rotate 270°");
		rotate270Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotateImage(270);
            }
        });

		JMenuItem flipHorizontalItem = new JMenuItem("Flip Horizontal");
		flipHorizontalItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipImage(true);
            }
        });

		JMenuItem flipVerticalItem = new JMenuItem("Flip Vertical");
		flipVerticalItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipImage(false);
            }
        });

		JMenuItem scaleItem = new JMenuItem("Scale Image");
		scaleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scaleImage(1.5);
            }
        });
		
		//----------------------Effects----------------------
        JMenuItem grayscaleItem = new JMenuItem("Grayscale");
        grayscaleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyGrayscale();
            }
        });

        JMenuItem invertItem = new JMenuItem("Invert Colors");
        invertItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invertColors();
            }
        });
		
        JMenuItem brightnessItem = new JMenuItem("Adjust Brightness");
        brightnessItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adjustBrightness();
            }
        });

        JMenuItem sepiaItem = new JMenuItem("Sepia");
        sepiaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySepia();
            }
        });

        JMenuItem edgeDetectItem = new JMenuItem("Edge Detection");
        edgeDetectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyEdgeDetection();
            }
        });
		
		JMenuItem pixelizeItem = new JMenuItem("Pixelize Effect");
        pixelizeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyPixelize(); //block size = 10 pixels
            }
        });

        JMenuItem scrambleItem = new JMenuItem("Scramble Effect");
        scrambleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyScramble();
            }
        });
		
		JMenuItem cropImageItem = new JMenuItem("Auto Crop Image");
        cropImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               autoCrop();
            }
        });
		
		JMenuItem upscaleItem = new JMenuItem("Upscale Image");
        upscaleItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               upscaleImage(2); //upscale x2
            }
        });
		
		JMenuItem upscaleItem2 = new JMenuItem("Upscale & Sharpen Image");
        upscaleItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               upscaleAndSharpen(2);
            }
        });
		
		JMenuItem sharpenItem = new JMenuItem("Sharpen");
        sharpenItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applySharpen();
            }
        });

        JMenuItem blurItem = new JMenuItem("Blur");
        blurItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyBlur();
            }
        });
		
		JMenuItem hdrItem = new JMenuItem("HDR Effect");
		hdrItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyHDREffect();
            }
        });
		
		JMenuItem glitchItem = new JMenuItem("Glitch Effect");
		glitchItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyGlitchEffect();
            }
        });

		JMenuItem cyberpunkItem = new JMenuItem("Cyberpunk Color Shift");
		cyberpunkItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyCyberpunkColorShift();
            }
        });
		
		JMenuItem roughBlurItem = new JMenuItem("Rough Blur");
		roughBlurItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyRoughBlur();
            }
        });
		
		JMenuItem oilPaintingItem = new JMenuItem("Spray Paint Effect");
		oilPaintingItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyOilPaintingEffect();
            }
        });

		JMenuItem depthEffectItem = new JMenuItem("Depth Effect");
		depthEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyDepthEffect();
            }
        });
		
		JMenuItem ambientOcclusionItem = new JMenuItem("Ambient Occlusion Effect");
		ambientOcclusionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyAmbientOcclusionEffect();
            }
        });

		JMenuItem lineArtItem = new JMenuItem("Line Art Effect");
		lineArtItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               applyLineArtEffect();
            }
        });
		
		JMenuItem glowEffectItem = new JMenuItem("Glow");
		glowEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              applyPixelColorGlowEffect(image, 0.3f);
            }
        });
	
		JMenuItem textReplaceEffectItem = new JMenuItem("Text Pixel Replacer");
		textReplaceEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              applyTextReplacerEffect();
            }
        });
		
		JMenuItem textImageItem = new JMenuItem("Import Text as Image");
		textImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              importTextAsImage(new File("text_image.txt"));
            }
        });
		
		JMenuItem thermoEffectItem = new JMenuItem("Thermo Infrared Effect");
		thermoEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              applyThermoInfraredEffect();
            }
        });
		
		JMenuItem explosionEffectItem = new JMenuItem("Explosion Pixel Effect");
		explosionEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              applyExplosionEffect();
            }
        });
		
		JMenuItem renaissanceEffectItem = new JMenuItem("Renaissance Painting Effect");
		renaissanceEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              applyRenaissancePaintingEffect();
            }
        });
		
		JMenuItem wireframeEffectItem = new JMenuItem("Polygonal Wireframe Effect");
		wireframeEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              applyPolygonalWireframeEffect();
            }
        });
		
		JMenuItem crackingEffectItem = new JMenuItem("Procedural Cracking Effect");
		crackingEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              applyCrackingEffect();
            }
        });
		
		JMenuItem polygonEffectItem = new JMenuItem("Triangular Polygon Effect");
		polygonEffectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              applyTriangularPolygonEffect();
            }
        });

		JMenuItem colorMaskEffectItem = new JMenuItem("Color Mask"); //add a slider for this
		colorMaskEffectItem.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
              applyColorMaskEffect(Color.BLUE);
              applyColorMaskEffect(Color.RED); //isolate red tones
            }
        });

		JMenuItem sphereFoldEffectItem = new JMenuItem("Sphere Fold Effect");
		sphereFoldEffectItem.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
				applySphereFoldEffect(120, 180); //inner radius, outer
            }
        });

		JMenuItem diabloEffectItem = new JMenuItem("D2 Color Effect");
		diabloEffectItem.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
				applyD2ColorEffect();
            }
        });

		JMenuItem bitColorEffectItem = new JMenuItem("8-Bit Color Effect");
		bitColorEffectItem.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
				apply8BitColorEffect();
            }
        });

		JMenuItem parallaxImageEffectItem = new JMenuItem("Parallax Effect");
		parallaxImageEffectItem.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
				apply3DParallaxEffect(25); //base strength
            }
        });

		JMenuItem gaussianBlurEffectItem = new JMenuItem("Gaussian Blur");
		gaussianBlurEffectItem.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
				//gaussianBlur(image, 10);
            }
        });

		//FileMenu
        fileMenu.add(loadImageItem);
        fileMenu.add(saveImageItem);
		fileMenu.add(reloadImageItem);
		fileMenu.add(toggleSettingsItem);
		fileMenu.add(toggleSettingsItem2);
		//Edit Menu
		editMenu.add(sharpenItem);
        editMenu.add(blurItem);
		editMenu.add(gaussianBlurEffectItem);
		editMenu.add(roughBlurItem);
		editMenu.add(brightnessItem);
        editMenu.add(grayscaleItem);
        editMenu.add(invertItem);
        editMenu.add(sepiaItem);
        editMenu.add(edgeDetectItem);
		editMenu.add(lineArtItem);
        editMenu.add(pixelizeItem);
		editMenu.add(bitColorEffectItem);
        editMenu.add(cropImageItem);
		editMenu.add(upscaleItem);
		editMenu.add(upscaleItem2);
		editMenu.add(cyberpunkItem);
		editMenu.add(oilPaintingItem);
		//Special Effects
		fxMenu.add(ambientOcclusionItem);
		fxMenu.add(hdrItem);
		fxMenu.add(depthEffectItem);
		fxMenu.add(glowEffectItem);
		fxMenu.add(scrambleItem);
		fxMenu.add(glitchItem);
		fxMenu.add(thermoEffectItem);
		fxMenu.add(explosionEffectItem);
		fxMenu.add(renaissanceEffectItem);
		fxMenu.add(wireframeEffectItem);
		fxMenu.add(crackingEffectItem);
		fxMenu.add(polygonEffectItem);
		fxMenu.add(colorMaskEffectItem);
		fxMenu.add(sphereFoldEffectItem);
		fxMenu.add(diabloEffectItem);
		fxMenu.add(parallaxImageEffectItem);
		fxMenu.add(textReplaceEffectItem);
		fxMenu.add(textImageItem);
		//Transform Menu
		transformMenu.add(rotate90Item);
		transformMenu.add(rotate180Item);
		transformMenu.add(rotate270Item);
		transformMenu.add(flipHorizontalItem);
		transformMenu.add(flipVerticalItem);
		transformMenu.add(scaleItem);
		//Menu Bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(fxMenu);
		menuBar.add(transformMenu);
        setJMenuBar(menuBar);
    }
	
	private void loadImage() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			imagePath = file.getAbsolutePath(); //save path for future reloads

			try {
				image = ImageIO.read(file);
				originalImage = ImageIO.read(file);
				imageLabel.setIcon(new ImageIcon(image));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

    private void saveImage() {
        if (image == null) {
            JOptionPane.showMessageDialog(this, "No image to save!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
	
	private void reloadImage() {
		if (imagePath != null) {
			try {
				image = ImageIO.read(new File(imagePath)); //reload saved file path
				imageLabel.setIcon(new ImageIcon(image));
				repaint(); //refresh UI
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error reloading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "No image path stored!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//TRANSFORMS
	private void rotateImage(int angle) {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage rotatedImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = rotatedImage.createGraphics();

		g2d.rotate(Math.toRadians(angle), height / 2.0, width / 2.0);
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		image = rotatedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}

	private void flipImage(boolean horizontal) {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = flippedImage.createGraphics();

		if (horizontal) {
			g2d.drawImage(image, 0, 0, width, height, width, 0, 0, height, null);
		} else {
			g2d.drawImage(image, 0, 0, width, height, 0, height, width, 0, null);
		}
		g2d.dispose();

		image = flippedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}

	private void scaleImage(double scaleFactor) {
		if (image == null) return;

		int newWidth = (int) (image.getWidth() * scaleFactor);
		int newHeight = (int) (image.getHeight() * scaleFactor);
		BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = scaledImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
		g2d.dispose();

		image = scaledImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	//EFFECTS
    private void applyGrayscale() {
        if (image == null) {
            JOptionPane.showMessageDialog(this, "No image loaded!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int gray = (r + g + b) / 3;
                int newRgb = (gray << 16) | (gray << 8) | gray;
                image.setRGB(x, y, newRgb);
            }
        }
        imageLabel.setIcon(new ImageIcon(image));
    }

    private void invertColors() {
        if (image == null) {
            JOptionPane.showMessageDialog(this, "No image loaded!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = 255 - ((rgb >> 16) & 0xFF);
                int g = 255 - ((rgb >> 8) & 0xFF);
                int b = 255 - (rgb & 0xFF);
                int newRgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, newRgb);
            }
        }
        imageLabel.setIcon(new ImageIcon(image));
    }
	
	 private void adjustBrightness() {
        if (image == null) return;
		int brightness = brightSlider.getValue();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = Math.min(255, Math.max(0, ((rgb >> 16) & 0xFF) + brightness));
                int g = Math.min(255, Math.max(0, ((rgb >> 8) & 0xFF) + brightness));
                int b = Math.min(255, Math.max(0, (rgb & 0xFF) + brightness));
                int newRgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, newRgb);
            }
        }
        imageLabel.setIcon(new ImageIcon(image));
    }

    private void applySepia() {
        if (image == null) return;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int tr = Math.min(255, (int) (0.393 * r + 0.769 * g + 0.189 * b));
                int tg = Math.min(255, (int) (0.349 * r + 0.686 * g + 0.168 * b));
                int tb = Math.min(255, (int) (0.272 * r + 0.534 * g + 0.131 * b));

                int newRgb = (tr << 16) | (tg << 8) | tb;
                image.setRGB(x, y, newRgb);
            }
        }
        imageLabel.setIcon(new ImageIcon(image));
    }

	private void applyEdgeDetection() {
        if (image == null) {
            JOptionPane.showMessageDialog(this, "No image loaded!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] sobelY = {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int sumX = 0, sumY = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int rgb = image.getRGB(x + i, y + j);
                        int gray = (rgb >> 16) & 0xFF; //extracting only the red channel for grayscale
                        sumX += gray * sobelX[i + 1][j + 1];
                        sumY += gray * sobelY[i + 1][j + 1];
                    }
                }

                int edgeValue = Math.min(255, (int) Math.sqrt(sumX * sumX + sumY * sumY));
                int newRgb = (edgeValue << 16) | (edgeValue << 8) | edgeValue;
                edgeImage.setRGB(x, y, newRgb);
            }
        }

        image = edgeImage;
        imageLabel.setIcon(new ImageIcon(image));
    }
	
	private void applyPixelize() {
        if (image == null) return;
		float pixelSize = pixelSlider.getValue(); //adjust pixel size
        for (int x = 0; x < image.getWidth(); x += pixelSize) {
            for (int y = 0; y < image.getHeight(); y += pixelSize) {
                int avgColor = image.getRGB(x, y);
                for (int i = 0; i < pixelSize && x + i < image.getWidth(); i++) {
                    for (int j = 0; j < pixelSize && y + j < image.getHeight(); j++) {
                        image.setRGB(x + i, y + j, avgColor);
                    }
                }
            }
        }
        imageLabel.setIcon(new ImageIcon(image));
    }

    private void applyScramble() { //implement a min max for random
		if (image == null) return;
		int amount = scramSlider.getValue(); 
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage swappedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();

		int blockSize = amount; //5 defines the pixel square size for swapping
		int[][] tempPixels = new int[width][height]; //temporary array for accurate swaps

		for (int x = 0; x < width; x++) { //copy original image pixels into temporary storage
			for (int y = 0; y < height; y++) {
				tempPixels[x][y] = image.getRGB(x, y);
			}
		}

		for (int x = 0; x < width; x += blockSize) {
			for (int y = 0; y < height; y += blockSize) {
				//choose a nearby swap location ensuring full coverage
				int swapX = Math.min(width - blockSize, Math.max(0, x + rand.nextInt(11) - 5));
				int swapY = Math.min(height - blockSize, Math.max(0, y + rand.nextInt(11) - 5));

				for (int i = 0; i < blockSize; i++) { //swap entire block of pixels while ensuring perfect rearrangement
					for (int j = 0; j < blockSize; j++) {
						int pixelX = Math.min(width - 1, x + i);
						int pixelY = Math.min(height - 1, y + j);
						int swapPixelX = Math.min(width - 1, swapX + i);
						int swapPixelY = Math.min(height - 1, swapY + j);

						swappedImage.setRGB(pixelX, pixelY, tempPixels[swapPixelX][swapPixelY]);
					}
				}
			}
		}

		image = swappedImage;
		imageLabel.setIcon(new ImageIcon(image));
    }

	private void autoCrop() {
     if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage transparentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		int backgroundColor = image.getRGB(0, 0); //assuming the top-left pixel represents background
		int tolerance = 30; //allow slight variations of color to be removed

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;
				
				int bgR = (backgroundColor >> 16) & 0xFF;
				int bgG = (backgroundColor >> 8) & 0xFF;
				int bgB = backgroundColor & 0xFF;

				//check if pixel color is within tolerance of the background color
				if (Math.abs(r - bgR) < tolerance && Math.abs(g - bgG) < tolerance && Math.abs(b - bgB) < tolerance) {
					transparentImage.setRGB(x, y, 0x00FFFFFF); // Transparent pixel
				} else {
					transparentImage.setRGB(x, y, (rgb & 0xFFFFFF) | (255 << 24)); // Preserve original pixel
				}
			}
		}

		image = transparentImage;
		imageLabel.setIcon(new ImageIcon(image));
    }
	
	private void upscaleImage(int scaleFactor) {
        if (image == null) return;

        int newWidth = image.getWidth() * scaleFactor;
        int newHeight = image.getHeight() * scaleFactor;

        BufferedImage upscaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = upscaledImage.createGraphics();

        //bicubic interpolation for smooth refinement
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        image = upscaledImage;
        imageLabel.setIcon(new ImageIcon(image));
    }
	
	private void upscaleAndSharpen(int scaleFactor) {
        if (image == null) return;

        int newWidth = image.getWidth() * scaleFactor;
        int newHeight = image.getHeight() * scaleFactor;

        BufferedImage upscaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = upscaledImage.createGraphics();

        //refined upscaling
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        image = applySharpening(upscaledImage);
        imageLabel.setIcon(new ImageIcon(image));
    }

    private BufferedImage applySharpening(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage sharpenedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        float[] sharpenKernel = {
                -1, -1, -1,
                -1, 9, -1,
                -1, -1, -1
        };

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int sumR = 0, sumG = 0, sumB = 0;
                int kernelIndex = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int rgb = img.getRGB(x + i, y + j);
                        int r = (rgb >> 16) & 0xFF;
                        int g = (rgb >> 8) & 0xFF;
                        int b = rgb & 0xFF;

                        sumR += r * sharpenKernel[kernelIndex];
                        sumG += g * sharpenKernel[kernelIndex];
                        sumB += b * sharpenKernel[kernelIndex];
                        kernelIndex++;
                    }
                }

                int newR = Math.min(255, Math.max(0, sumR));
                int newG = Math.min(255, Math.max(0, sumG));
                int newB = Math.min(255, Math.max(0, sumB));

                int newRgb = (newR << 16) | (newG << 8) | newB;
                sharpenedImage.setRGB(x, y, newRgb);
            }
        }

        return sharpenedImage;
    }
	
	private void applySharpen() {
		if (image == null) return;
		float intensity = sharpenSlider.getValue() / 5.0f; //adjust intensity dynamically
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage sharpenedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		float[] sharpenKernel = {  
			-0.5f * intensity, -0.5f * intensity, -0.5f * intensity,  
			-0.5f * intensity,  5f * intensity,   -0.5f * intensity,  
			-0.5f * intensity, -0.5f * intensity, -0.5f * intensity  
		};

		Kernel kernel = new Kernel(3, 3, sharpenKernel);
		ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		sharpenedImage = convolve.filter(image, null);

		BufferedImage smoothImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		float[] blurKernel = {  //apply soft blending to reduce harshness
			0.1f, 0.1f, 0.1f,  
			0.1f, 0.2f, 0.1f,  
			0.1f, 0.1f, 0.1f  
		};
		
		Kernel blurKernelMatrix = new Kernel(3, 3, blurKernel);
		ConvolveOp smoothOp = new ConvolveOp(blurKernelMatrix, ConvolveOp.EDGE_NO_OP, null);
		smoothImage = smoothOp.filter(sharpenedImage, null);

		image = smoothImage;
		imageLabel.setIcon(new ImageIcon(image));
    }

    private void applyBlur() {
        if (image == null) return;
        float strength = blurSlider.getValue() / 10.0f; //adjust blur dynamically

        float[] blurKernel = {
            strength, strength, strength,
            strength, strength, strength,
            strength, strength, strength
        };

        image = applyKernel(image, blurKernel);
        imageLabel.setIcon(new ImageIcon(image));
    }

    private BufferedImage applyKernel(BufferedImage img, float[] kernel) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage processedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                float sumR = 0, sumG = 0, sumB = 0;
                int kernelIndex = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int rgb = img.getRGB(x + i, y + j);
                        int r = (rgb >> 16) & 0xFF;
                        int g = (rgb >> 8) & 0xFF;
                        int b = rgb & 0xFF;

                        sumR += r * kernel[kernelIndex];
                        sumG += g * kernel[kernelIndex];
                        sumB += b * kernel[kernelIndex];
                        kernelIndex++;
                    }
                }

                int newR = Math.min(255, Math.max(0, (int) sumR));
                int newG = Math.min(255, Math.max(0, (int) sumG));
                int newB = Math.min(255, Math.max(0, (int) sumB));

                int newRgb = (newR << 16) | (newG << 8) | newB;
                processedImage.setRGB(x, y, newRgb);
            }
        }

        return processedImage;
    }
	
	private void applyHDREffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage hdrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				//enhance contrast dynamically
				r = Math.min(255, (int)(r * 1.2 + 10));
				g = Math.min(255, (int)(g * 1.2 + 10));
				b = Math.min(255, (int)(b * 1.2 + 10));

				int newRgb = (r << 16) | (g << 8) | b;
				hdrImage.setRGB(x, y, newRgb);
			}
		}

		image = hdrImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyGlitchEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage glitchedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();

		for (int x = 0; x < width; x++) { //random pixel shift effect
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				int shiftX = rand.nextInt(4) - 2; //slight random horizontal shift
				int shiftY = rand.nextInt(4) - 2; //slight random vertical shift
				int newX = Math.min(width - 1, Math.max(0, x + shiftX));
				int newY = Math.min(height - 1, Math.max(0, y + shiftY));
				// Color channel distortion
				int noise = rand.nextInt(30) - 15; //random color channel interference
				r = Math.max(0, Math.min(255, r + noise));
				g = Math.max(0, Math.min(255, g - noise));
				b = Math.max(0, Math.min(255, b + noise));
				// Line artifacts
				if (rand.nextInt(100) < 5) { // 5% chance to inject scanline distortion
					r = rand.nextBoolean() ? 0 : 255;
					g = rand.nextBoolean() ? 0 : 255;
					b = rand.nextBoolean() ? 0 : 255;
				}

				int newRgb = (r << 16) | (g << 8) | b;
				glitchedImage.setRGB(newX, newY, newRgb);
			}
		}

		image = glitchedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyCyberpunkColorShift() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage cyberpunkImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				r = Math.min(255, r + 40); //shift colors to neon purple glow
				g = Math.max(0, g - 50);
				b = Math.min(255, b + 100);

				int newRgb = (r << 16) | (g << 8) | b;
				cyberpunkImage.setRGB(x, y, newRgb);
			}
		}

		image = cyberpunkImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyRoughBlur() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage paintedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();

		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				int strokeLength = rand.nextInt(2) + 2; //3 + 2
				int avgR = (r + (image.getRGB(Math.min(x + strokeLength, width - 1), y) >> 16 & 0xFF)) / 2;
				int avgG = (g + (image.getRGB(Math.min(x + strokeLength, width - 1), y) >> 8 & 0xFF)) / 2;
				int avgB = (b + (image.getRGB(Math.min(x + strokeLength, width - 1), y) & 0xFF)) / 2;

				int blendFactor = rand.nextInt(40) + 10; //adjust intensity randomly
				avgR = Math.max(0, Math.min(255, (avgR * (100 - blendFactor) + r * blendFactor) / 100));
				avgG = Math.max(0, Math.min(255, (avgG * (100 - blendFactor) + g * blendFactor) / 100));
				avgB = Math.max(0, Math.min(255, (avgB * (100 - blendFactor) + b * blendFactor) / 100));

				int texture = rand.nextInt(10) - 5;
				avgR = Math.max(0, Math.min(255, avgR + texture));
				avgG = Math.max(0, Math.min(255, avgG + texture));
				avgB = Math.max(0, Math.min(255, avgB + texture));

				int newRgb = (avgR << 16) | (avgG << 8) | avgB;
				paintedImage.setRGB(x, y, newRgb);
			}
		}

		image = paintedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyOilPaintingEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage oilPaintImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();

		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				int strokeFlow = rand.nextInt(10) + 2; //simulated brush strokes using directional smoothing
				int avgR = (r + (image.getRGB(Math.min(x + strokeFlow, width - 1), y) >> 16 & 0xFF)) / 2;
				int avgG = (g + (image.getRGB(Math.min(x + strokeFlow, width - 1), y) >> 8 & 0xFF)) / 2;
				int avgB = (b + (image.getRGB(Math.min(x + strokeFlow, width - 1), y) & 0xFF)) / 2;

				int blendFactor = rand.nextInt(100) + 10; //layered color blending to replicate soft paint texture
				avgR = Math.max(0, Math.min(255, (avgR * (100 - blendFactor) + r * blendFactor) / 100));
				avgG = Math.max(0, Math.min(255, (avgG * (100 - blendFactor) + g * blendFactor) / 100));
				avgB = Math.max(0, Math.min(255, (avgB * (100 - blendFactor) + b * blendFactor) / 100));

				int texture = rand.nextInt(15) - 7; //canvas texture overlay with subtle imperfections
				avgR = Math.max(0, Math.min(255, avgR + texture));
				avgG = Math.max(0, Math.min(255, avgG + texture));
				avgB = Math.max(0, Math.min(255, avgB + texture));

				int newRgb = (avgR << 16) | (avgG << 8) | avgB;
				oilPaintImage.setRGB(x, y, newRgb);
			}
		}

		image = oilPaintImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyDepthEffect() {
		if (image == null) return;
			int width = image.getWidth();
			int height = image.getHeight();
			BufferedImage depthImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int rgb = image.getRGB(x, y);
					int r = (rgb >> 16) & 0xFF;
					int g = (rgb >> 8) & 0xFF;
					int b = rgb & 0xFF;

					int depthFactor = (int) ((double) y / height * 40); //calculate depth blur based on height
					depthFactor = Math.min(50, depthFactor); //cap intensity
					
					int blurRange = Math.min(5, depthFactor / 5); //apply Gaussian blur using neighboring pixels
					int totalR = 0, totalG = 0, totalB = 0, count = 0;

					for (int dx = -blurRange; dx <= blurRange; dx++) {
						for (int dy = -blurRange; dy <= blurRange; dy++) {
							int newX = Math.min(width - 1, Math.max(0, x + dx));
							int newY = Math.min(height - 1, Math.max(0, y + dy));
							int neighborRGB = image.getRGB(newX, newY);
							
							totalR += (neighborRGB >> 16) & 0xFF;
							totalG += (neighborRGB >> 8) & 0xFF;
							totalB += neighborRGB & 0xFF;
							count++;
						}
					}

					r = Math.max(0, Math.min(255, totalR / count));
					g = Math.max(0, Math.min(255, totalG / count));
					b = Math.max(0, Math.min(255, totalB / count));

					int newRgb = (r << 16) | (g << 8) | b;
					depthImage.setRGB(x, y, newRgb);
				}
			}
		image = depthImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyAmbientOcclusionEffect() {
		if (image == null) return;
		int intensity = ambOSlider.getValue();
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage occlusionImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				int shadowStrength = 0; //analyze neighboring pixels to detect shadow zones
				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = -1; dy <= 1; dy++) {
						int newX = Math.min(width - 1, Math.max(0, x + dx));
						int newY = Math.min(height - 1, Math.max(0, y + dy));
						int neighborRGB = image.getRGB(newX, newY);
						
						int neighborBrightness = ((neighborRGB >> 16) & 0xFF + (neighborRGB >> 8) & 0xFF + (neighborRGB & 0xFF)) / 3;
						shadowStrength += Math.max(0, 255 - neighborBrightness); //darker areas receive stronger shading
					}
				}

				shadowStrength /= 9; //normalize the shading intensity
				shadowStrength = Math.min(intensity, shadowStrength / 4); //max depth

				r = Math.max(0, r - shadowStrength);
				g = Math.max(0, g - shadowStrength);
				b = Math.max(0, b - shadowStrength);

				int newRgb = (r << 16) | (g << 8) | b;
				occlusionImage.setRGB(x, y, newRgb);
			}
		}

		image = occlusionImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyLineArtEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage lineArtImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;
				int brightness = (r + g + b) / 3;

				//edge detection using contrast differences
				int edgeStrength = Math.abs(brightness - ((image.getRGB(x + 1, y) >> 16) & 0xFF));
				edgeStrength = Math.max(edgeStrength, Math.abs(brightness - ((image.getRGB(x, y + 1) >> 8) & 0xFF)));

				int newRgb = (edgeStrength > 40) ? 0x000000 : 0xFFFFFF; //apply high contrast: Black for detected edges, White elsewhere
				lineArtImage.setRGB(x, y, newRgb);
			}
		}

		image = lineArtImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	
	private void applyPixelColorGlowEffect(BufferedImage image, float intensity) {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage glowImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = glowImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		
		//create falloff gradient overlay
		RadialGradientPaint glowFalloff = new RadialGradientPaint(
			new Point2D.Float(width / 2f, height / 2f), //glow center
			Math.max(width, height) / 2f, //glow radius
			new float[]{0f, 0.5f, 1f}, //gradient stops
			new Color[]{new Color(255, 255, 255, 100), new Color(255, 255, 255, 50), new Color(255, 255, 255, 0)}
		);

		g.setPaint(glowFalloff);
		g.setComposite(AlphaComposite.SrcOver.derive(0.3f)); //soft blending
		g.fillRect(0, 0, width, height);

		g.dispose();

		image = glowImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void exportTextToFile(String asciiText) {
		if (asciiText == null || asciiText.isEmpty()) return;

		try {
			FileWriter writer = new FileWriter("text_image.txt");
			writer.write(asciiText);
			writer.close();
			System.out.println("Text file saved successfully!");
		} catch (IOException e) {
			System.err.println("Error saving text file: " + e.getMessage());
		}
	}

	private void applyTextReplacerEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		char[] characters = {'@', '#', '8', '&', '*', '+', ':', '.', ' '}; //dark to light mapping

		StringBuilder asciiArt = new StringBuilder();
		for (int y = 0; y < height; y += 6) { //step size determines resolution
			for (int x = 0; x < width; x += 3) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;
				int brightness = (r + g + b) / 3;

				int charIndex = (brightness * (characters.length - 1)) / 255; //assign a character based on pixel brightness
				asciiArt.append(characters[charIndex]);
			}
			asciiArt.append("\n"); //new line for correct alignment
		}

		//display or store the text representation System.out.println(asciiArt.toString());
		exportTextToFile(asciiArt.toString());
	}
	
	
	private void importTextAsImage(File textFile) {
		if (textFile == null || !textFile.exists()) return;

		try {
			List<String> lines = new ArrayList<>(); //read text file content
			BufferedReader reader = new BufferedReader(new FileReader(textFile));
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();

			int textHeight = lines.size() * 20; //determine required canvas size, Approximate height per line
			Font font = new Font("Monospaced", Font.BOLD, 20);
			
			BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			Graphics2D gTemp = tempImage.createGraphics();
			gTemp.setFont(font);

			int textWidth = 0; //measure maximum text width
			for (String textLine : lines) {
				textWidth = Math.max(textWidth, gTemp.getFontMetrics().stringWidth(textLine));
			}
			gTemp.dispose();

			//set up dynamically sized image canvas
			BufferedImage textImage = new BufferedImage(textWidth + 50, textHeight + 50, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = textImage.createGraphics();

			g.setColor(Color.BLACK);
			g.fillRect(0, 0, textImage.getWidth(), textImage.getHeight());

			g.setFont(font);
			g.setColor(Color.WHITE);

			int textX = 25, textY = 40; //margins for spacing
			for (String textLine : lines) { //draw text dynamically within resized canvas
				g.drawString(textLine, textX, textY);
				textY += 20; //sdjust spacing for each line
			}

			g.dispose();
			imageLabel.setIcon(new ImageIcon(textImage)); //display resized image

			File outputFile = new File("text_image_output.png"); //save generated text image
			ImageIO.write(textImage, "png", outputFile);
			System.out.println("Text image saved successfully: " + outputFile.getAbsolutePath());

		} catch (IOException e) {
			System.err.println("Error processing text file: " + e.getMessage());
		}
	}
	
	private void applyThermoInfraredEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage infraredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;
				int brightness = (r + g + b) / 3;
				
				Color heatColor; //map brightness to heat color gradient
				if (brightness > 220) {
					heatColor = new Color(255, 255, 0); //yellow (High Heat)
				} else if (brightness > 180) {
					heatColor = new Color(255, 165, 0); //orange (Medium Heat)
				} else if (brightness > 100) {
					heatColor = new Color(255, 0, 0); //red (Warm)
				} else if (brightness > 50) {
					heatColor = new Color(75, 0, 130); //purple (Cool)
				} else {
					heatColor = new Color(0, 0, 255); //blue (Cold)
				}

				infraredImage.setRGB(x, y, heatColor.getRGB());
			}
		}

		image = infraredImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyExplosionEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage explodedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();

		//define explosion origin (center point)
		int centerX = width / 2;
		int centerY = height / 2;
		int explosionStrength = 20; //adjusts pixel dispersion radius

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);

				int dx = rand.nextInt(explosionStrength) - explosionStrength / 2; //calculate displacement based on distance from explosion center
				int dy = rand.nextInt(explosionStrength) - explosionStrength / 2;

				int newX = Math.min(width - 1, Math.max(0, x + dx));
				int newY = Math.min(height - 1, Math.max(0, y + dy));

				explodedImage.setRGB(newX, newY, rgb);
			}
		}

		image = explodedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}

	private void applyRenaissancePaintingEffect() { //this one is a joke
	   if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage cubismImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = cubismImage.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		List<Point> featurePoints = new ArrayList<>();
		Random rand = new Random();

		//extract key feature points safely
		for (int x = 0; x < width; x += rand.nextInt(30) + 20) {
			for (int y = 0; y < height; y += rand.nextInt(30) + 20) {
				featurePoints.add(new Point(x, y));
			}
		}

		//generate cubist-style fragmented polygons without exceeding bounds
		for (int i = 0; i < featurePoints.size() - 2; i++) {
			Point p1 = featurePoints.get(i);
			Point p2 = featurePoints.get(i + 1);
			Point p3 = featurePoints.get(i + 2);

			//ensure feature points are within valid image boundaries
			p1.x = Math.min(width - 1, Math.max(0, p1.x));
			p1.y = Math.min(height - 1, Math.max(0, p1.y));
			p2.x = Math.min(width - 1, Math.max(0, p2.x));
			p2.y = Math.min(height - 1, Math.max(0, p2.y));
			p3.x = Math.min(width - 1, Math.max(0, p3.x));
			p3.y = Math.min(height - 1, Math.max(0, p3.y));

			//select base color from the original image
			int baseRGB = image.getRGB(p1.x, p1.y);
			Color cubistColor = new Color((baseRGB >> 16) & 0xFF, (baseRGB >> 8) & 0xFF, baseRGB & 0xFF);

			//apply angular distortion while ensuring safe translation
			int shiftX = rand.nextInt(20) - 10;
			int shiftY = rand.nextInt(20) - 10;
			p1.translate(shiftX, shiftY);
			p2.translate(-shiftX, shiftY);
			p3.translate(shiftX, -shiftY);

			//validate points after transformations
			p1.x = Math.min(width - 1, Math.max(0, p1.x));
			p1.y = Math.min(height - 1, Math.max(0, p1.y));
			p2.x = Math.min(width - 1, Math.max(0, p2.x));
			p2.y = Math.min(height - 1, Math.max(0, p2.y));
			p3.x = Math.min(width - 1, Math.max(0, p3.x));
			p3.y = Math.min(height - 1, Math.max(0, p3.y));

			//draw fragmented cubist polygon
			g.setColor(cubistColor);
			g.fillPolygon(new int[]{p1.x, p2.x, p3.x}, new int[]{p1.y, p2.y, p3.y}, 3);
		}

		g.dispose();
		image = cubismImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyPolygonalWireframeEffect() {
	   if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage webImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = webImage.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		List<Point> edgePoints = new ArrayList<>();
		Set<String> connectedPairs = new HashSet<>();
		Random rand = new Random();

		//optimized edge detection (using Sobel Operator)
		for (int x = 1; x < width - 1; x += 8) {
			for (int y = 1; y < height - 1; y += 8) {
				int rgb = image.getRGB(x, y);
				float gray = (rgb >> 16 & 0xFF) * 0.3f + (rgb >> 8 & 0xFF) * 0.59f + (rgb & 0xFF) * 0.11f;

				float gx = Math.abs(gray - (image.getRGB(x + 1, y) >> 16 & 0xFF));
				float gy = Math.abs(gray - (image.getRGB(x, y + 1) >> 16 & 0xFF));

				float edgeStrength = gx + gy;
				if (edgeStrength > 25) { //65
					edgePoints.add(new Point(x, y)); //store detected edge points
				}
			}
		}

		//process connections one line at a time to prevent overlap
		for (int i = 0; i < edgePoints.size(); i++) {
			Point p1 = edgePoints.get(i);
			Point bestMatch = null;
			double bestDistance = Double.MAX_VALUE;

			//find the closest valid point for connection
			for (int j = i + 1; j < edgePoints.size(); j++) {
				Point p2 = edgePoints.get(j);
				double distance = p1.distance(p2);
				if (distance > 10 && distance < 70 && distance < bestDistance) { //15, 50
					bestMatch = p2;
					bestDistance = distance;
				}
			}

			//draw a single optimized connection per iteration
			if (bestMatch != null) {
				String pairKey = p1.x + "," + p1.y + "-" + bestMatch.x + "," + bestMatch.y;
				if (!connectedPairs.contains(pairKey)) {
					g.setColor(new Color(rand.nextInt(200) + 50, rand.nextInt(200) + 50, rand.nextInt(200) + 50));
					g.drawLine(p1.x, p1.y, bestMatch.x, bestMatch.y);
					connectedPairs.add(pairKey);
				}
			}
		}

		g.dispose();
		image = webImage;
		imageLabel.setIcon(new ImageIcon(image));
	}

	private void applyCrackingEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage crackedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = crackedImage.createGraphics();

		g.drawImage(image, 0, 0, null); //copy original image
		g.setColor(new Color(50, 50, 50, 150)); //semi-transparent dark crack color

		Random rand = new Random();
		int crackCount = 15; //number of fractures

		for (int i = 0; i < crackCount; i++) {
			int startX = rand.nextInt(width);
			int startY = rand.nextInt(height);
			int length = rand.nextInt(100) + 50; //random crack length
			int angle = rand.nextInt(360); //direction of crack

			for (int j = 0; j < length; j++) {
				int offsetX = (int) (Math.cos(Math.toRadians(angle)) * j);
				int offsetY = (int) (Math.sin(Math.toRadians(angle)) * j);

				int x = Math.min(width - 1, Math.max(0, startX + offsetX));
				int y = Math.min(height - 1, Math.max(0, startY + offsetY));

				g.drawLine(startX, startY, x, y); //draw crack line
				startX = x;
				startY = y;

				if (rand.nextInt(10) > 7) { //branching cracks
					angle += rand.nextInt(40) - 20; //slight direction change
				}
			}
		}

		g.dispose();
		image = crackedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyTriangularPolygonEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage polygonImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = polygonImage.createGraphics();
		Random rand = new Random();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		int baseSize = 20; //base size for polygons

		for (int x = 0; x < width; x += baseSize) {
			for (int y = 0; y < height; y += baseSize) {
				//apply random size variation within a small range
				int variation = rand.nextInt(10) - 5; //random adjustment
				int size = Math.max(10, baseSize + variation); //ensure reasonable sizing

				int x1 = x; //define triangle vertices using adjusted sizes
				int y1 = y;
				int x2 = Math.min(width - 1, x + size);
				int y2 = y;
				int x3 = x;
				int y3 = Math.min(height - 1, y + size);

				int avgX = (x1 + x2 + x3) / 3; //sample color from the approximate triangle region
				int avgY = (y1 + y2 + y3) / 3;
				int rgb = image.getRGB(Math.min(width - 1, avgX), Math.min(height - 1, avgY));
				
				g.setColor(new Color(rgb));
				g.fillPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
			}
		}

		g.dispose();
		image = polygonImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyColorMaskEffect(Color targetColor) {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage maskedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				int distance = Math.abs(targetColor.getRed() - r) +  //calculate color distance to target color
							   Math.abs(targetColor.getGreen() - g) + 
							   Math.abs(targetColor.getBlue() - b);

				//define a threshold for color similarity
				int threshold = 50; //adjust to widen or narrow selected color range

				if (distance < threshold) {
					maskedImage.setRGB(x, y, new Color(r, g, b).getRGB()); //keep target color
				} else {
					int gray = (r + g + b) / 3; //convert non-matching pixels to grayscale
					maskedImage.setRGB(x, y, new Color(gray, gray, gray).getRGB());
				}
			}
		}

		image = maskedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applySphereFoldEffect(float r, float R) {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage foldedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		int centerX = width / 2;
		int centerY = height / 2;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int dx = x - centerX;
				int dy = y - centerY;
				float len2 = dx * dx + dy * dy;
				float r2 = r * r;
				float R2 = R * R;

				float scaleFactor = 1.2f;
				if (len2 < r2) scaleFactor = R2 / r2;
				else if (len2 < R2) scaleFactor = R2 / len2;

				int newX = Math.min(width - 1, Math.max(0, centerX + (int) (dx * scaleFactor)));
				int newY = Math.min(height - 1, Math.max(0, centerY + (int) (dy * scaleFactor)));

				foldedImage.setRGB(newX, newY, image.getRGB(x, y));
			}
		}

		image = foldedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void applyD2ColorEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage diabloImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
		Random rand = new Random();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				//apply 8-bit color quantization
				r = (r / 32) * 32; //restrict to 8-bit step
				g = (g / 32) * 32;
				b = (b / 32) * 32;

				//darken tones slightly for gothic feel
				//r = Math.max(0, Math.min(255, (int) (r * 0.8 + 20))); 
				//g = Math.max(0, Math.min(255, (int) (g * 0.7 + 15)));
				//b = Math.max(0, Math.min(255, (int) (b * 0.6)));

				diabloImage.setRGB(x, y, new Color(r, g, b).getRGB());
			}
		}

		image = diabloImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void apply8BitColorEffect() {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage colorReducedImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB); //no alpha

		for (int x = 0; x < width; x++) {  //same as D2
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				r = (r / 32) * 32;
				g = (g / 32) * 32;
				b = (b / 32) * 32;

				colorReducedImage.setRGB(x, y, new Color(r, g, b).getRGB());
			}
		}

		image = colorReducedImage;
		imageLabel.setIcon(new ImageIcon(image));
	}
	
	private void apply3DParallaxEffect(float depthFactor) {
		if (image == null) return;

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage parallaxImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int centerX = width / 2;
		int centerY = height / 2;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;

				//compute depth displacement using simple luminance as height map
				float depth = (r + g + b) / 765.0f; //normalize based on brightness
				float offsetX = depthFactor * depth * ((x - centerX) / (float) width);
				float offsetY = depthFactor * depth * ((y - centerY) / (float) height);

				int newX = Math.min(width - 1, Math.max(0, x + (int) offsetX));
				int newY = Math.min(height - 1, Math.max(0, y + (int) offsetY));

				parallaxImage.setRGB(newX, newY, image.getRGB(x, y));
			}
		}

		image = parallaxImage;
		imageLabel.setIcon(new ImageIcon(image));
	}

    public static void main(String[] args) {
        new CustomImageEditor().setVisible(true);
    }
}

