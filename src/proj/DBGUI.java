package proj;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

public class DBGUI {

    //The ArrayLists simplify clearing labels
    private static final ArrayList<JComboBox> browseByMakerBoxes = new ArrayList<JComboBox>();
    private static final ArrayList<JComboBox> browseByPartsBoxes = new ArrayList<JComboBox>();
    //different displays for the different tabs
    private static final JTextArea partsDisplay_m = new JTextArea();
    private static final JTextArea partsDisplay_p = new JTextArea();
    private static final JTextArea carDisplay = new JTextArea();
    private static final String[] DEFAULT_LABELS = {"MAKE", "MODEL", "YEAR", "ENGINE", "PARTS", "VENDOR", "PART NUMBER"};
    //the following comboBoxes belong to the By Maker Tab
    private static final JComboBox make = new JComboBox();
    //Update boxes
    private static final JComboBox makeUpdateBox = new JComboBox();
    private static final JComboBox partUpdateBox = new JComboBox();
    private static final JComboBox model = new JComboBox();
    private static final JComboBox year = new JComboBox();
    private static final JComboBox engine = new JComboBox();
    private static final JComboBox parts = new JComboBox();

    //the following comboBoxes belong to the By Parts Tab
    private static final JComboBox vendor = new JComboBox();
    private static final JComboBox partNumber = new JComboBox();
    private static final JFrame frame = new JFrame();

    /**
     * Creates an empty GUI set up with two tabs, one to search by
     * maker and another to search for by parts number
     */
    public DBGUI() {
    	populateUpdateMake();
    	populateUpdatePart();
    	
        //creates the frame

        frame.setPreferredSize(new Dimension(800, 600));

        //creates the tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        frame.add(tabs);


 //////////////////////////////////////////////////////////////////////
        //Sets up update and delete fields for browse by make
        
        JPanel updateCarsFrame = new JPanel();
        JPanel updateCars = new JPanel();

        updateCarsFrame.setLayout(new BorderLayout());
        updateCars.setLayout(new GridLayout(0, 5));
        updateCars.setSize(200, 200);
        final JTextField updcyear = new JTextField();
        final JTextField updcengine = new JTextField();
        final JTextField updcparts = new JTextField();
        final JTextField updDesc = new JTextField();
        final JTextField updLitres = new JTextField();
        final JTextField updCubic = new JTextField();
        final JTextField addPartToCar = new JTextField();
        JButton addPartButton = new JButton("Add Part");
        JButton carupdSubmit = new JButton("Update");
        JButton cardelSubmit = new JButton("Delete");


        updateCars.add(new JLabel("Year"));
        updateCars.add(new JLabel("Description"));
        updateCars.add(new JLabel("Litres"));
        updateCars.add(new JLabel("Engine Type"));
        updateCars.add(new JLabel("Cubic"));


        updateCars.add(updcyear);
        updateCars.add(updDesc);
        updateCars.add(updLitres);
        updateCars.add(updcengine);
        updateCars.add(updCubic);

        
        

        updateCarsFrame.add(updateCars, BorderLayout.PAGE_START);
        updateCarsFrame.add(carupdSubmit, BorderLayout.CENTER);
        carDisplay.setColumns(40);
        carDisplay.setRows(10);
        updateCarsFrame.add(carDisplay, BorderLayout.PAGE_END);
        
        //////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////
        //Creates the panel for the browse by maker options////////////////////////////////  
        
        JPanel browseByMaker = new JPanel();
        browseByMaker.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
       browseByMaker.setLayout(new BorderLayout());
        make.addItem(DEFAULT_LABELS[0]);
        model.addItem(DEFAULT_LABELS[1]);
        year.addItem(DEFAULT_LABELS[2]);
        engine.addItem(DEFAULT_LABELS[3]);
        parts.addItem(DEFAULT_LABELS[4]);

        browseByMakerBoxes.add(make);
        browseByMakerBoxes.add(model);
        browseByMakerBoxes.add(year);
        browseByMakerBoxes.add(engine);
        browseByMakerBoxes.add(parts);
        make.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String choice = (String) ((JComboBox) event.getSource()).getSelectedItem();
                if (!choice.equals(DEFAULT_LABELS[0])) {
                    clearByMakerBoxes(1);
                    Main.send(choice, ScreenState.BROWSE_CAR_MAKERS);//send the choice out
                }


            }
        });
        model.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String choice = (String) ((JComboBox) event.getSource()).getSelectedItem();
                if (!choice.equals(DEFAULT_LABELS[1])) {
                    clearByMakerBoxes(2);
                    Main.send(choice, ScreenState.BROWSE_MODELS);
                }
            }
        });

        year.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String choice = (String) ((JComboBox) event.getSource()).getSelectedItem();
                if (!choice.equals(DEFAULT_LABELS[2])) {
                    clearByMakerBoxes(3);
                    Main.send(choice, ScreenState.BROWSE_YEARS);
                }

            }
        });

        engine.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String choice = (String) ((JComboBox) event.getSource()).getSelectedItem();
                if (!choice.equals(DEFAULT_LABELS[3])) {
                    clearByMakerBoxes(4);
                    Main.send(choice, ScreenState.BROWSE_ENGINE_TYPES);
                }
            }
        });

        parts.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String choice = (String) ((JComboBox) event.getSource()).getSelectedItem();
                if (!choice.equals(DEFAULT_LABELS[4])) {
                    Main.send(choice, ScreenState.BROWSE_PARTS_FOR_ENGINE);
                }
            }
        });
        
        
        carupdSubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String[] newCar = new String[7];
               // System.out.println( "BLAHBLAH");
                //newCar[0] = updcmodel.getText();
                newCar[1] = updcyear.getText();
                newCar[2] = updcengine.getText();
               // newCar[3] = updcparts.getText();
                newCar[4] = updDesc.getText();
                newCar[5] = updLitres.getText();
                newCar[6] = updCubic.getText();
               /* for(String e: newCar){
                    if(e.equals("")){
                        return;
                    }
                }
                */
                String[] oldCar = new String[6];
                oldCar[0] = (String) make.getSelectedItem();
                System.out.println(oldCar[0]);
                oldCar[1] = (String) model.getSelectedItem();
                oldCar[2] = (String) year.getSelectedItem();
                oldCar[3] = (String) engine.getSelectedItem();
                
                String part = (String) parts.getSelectedItem();
               oldCar[4] = part.substring(0, 3);
                oldCar[5] = part.substring(4);
                System.out.println(oldCar[4]  + oldCar[5]);
                try {
					Main.updateCar(newCar, oldCar);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
            }
        });
        
        //Delete Button
        cardelSubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String[] text = new String[5];
                text[0] = (String) make.getSelectedItem();
                text[1] = (String) model.getSelectedItem();
                text[2] = (String) year.getSelectedItem();
                text[3] = (String) engine.getSelectedItem();
                text[4] = (String) parts.getSelectedItem();
                for(String e: text){
                    if(e.equals("")){
                        return;
                    }
                        
                }
                Main.deleteCar(text);
                
            }
        });
        addPartButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String part = (String) addPartToCar.getText();
                String partMaker = part.substring(0, 3);
                String partNumber = part.substring(4, 7);
                System.out.println(partMaker + partNumber);
                String[] text = new String[5];
                text[0] = (String) make.getSelectedItem();
                text[1] = (String) model.getSelectedItem();
                text[2] = (String) year.getSelectedItem();
                text[3] = (String) engine.getSelectedItem();
                //text[4] = (String) parts.getSelectedItem();        
                
               Main.addPartToCar(partMaker, partNumber, text);
                
            }
        });
        JPanel boxPanel_m = new JPanel(new GridLayout(0,8));
        boxPanel_m.add(make);
        boxPanel_m.add(model);
        boxPanel_m.add(year);
        boxPanel_m.add(engine);
        boxPanel_m.add(parts);
        boxPanel_m.add(cardelSubmit);
        boxPanel_m.add(addPartButton);
        boxPanel_m.add(addPartToCar, BorderLayout.PAGE_END);
        
        browseByMaker.add(boxPanel_m, BorderLayout.PAGE_START);
        browseByMaker.add(updateCarsFrame, BorderLayout.PAGE_END);
        partsDisplay_m.setColumns(40);
        partsDisplay_m.setLineWrap(true);
        partsDisplay_m.setRows(10);
        partsDisplay_m.setWrapStyleWord(true);
        partsDisplay_m.setEditable(false);

        browseByMaker.add(partsDisplay_m, BorderLayout.CENTER);
        tabs.addTab("Browse by Maker", browseByMaker);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Sets up the update and delete fields for parts
        JPanel updatePartsFrame = new JPanel();
        JPanel updateParts = new JPanel();

        updatePartsFrame.setLayout(new BorderLayout());
        updateParts.setLayout(new GridLayout(0, 9));
        updateParts.setSize(200, 200);
        final JTextField updpmaker = new JTextField();
        final JTextField updPart = new JTextField();
        final JTextField updCore = new JTextField();
        final JTextField updInhead = new JTextField();
        final JTextField updOucon = new JTextField();
        final JTextField updOuthead = new JTextField();
        final JTextField updIncon = new JTextField();
        final JTextField updTmount = new JTextField();
        final JTextField updOilcool = new JTextField();
        final JTextField updPrice = new JTextField();
        final JTextField updAmount = new JTextField();
        JButton partsUpdSubmit = new JButton("Update");
        JButton delpartsSubmit = new JButton("Delete");
        updpmaker.setColumns(30);
        updPart.setColumns(30);

        //updateParts.add(new JLabel("Make"));
       // updateParts.add(new JLabel("Part Number"));
        updateParts.add(new JLabel("Core"));
        updateParts.add(new JLabel("Inhead"));
        updateParts.add(new JLabel("Outhead"));
        updateParts.add(new JLabel("InCon"));
        updateParts.add(new JLabel("OuCon"));
        updateParts.add(new JLabel("TMount"));
        updateParts.add(new JLabel("OilCool"));
        updateParts.add(new JLabel("Price"));
        updateParts.add(new JLabel("Amount"));
        
        //updateParts.add(partUpdateBox);
       // updateParts.add(updPart);
        updateParts.add(updCore);
        updateParts.add(updInhead);
        updateParts.add(updOuthead);
        updateParts.add(updIncon);
        updateParts.add(updOucon);
        updateParts.add(updTmount);
        updateParts.add(updOilcool);
        updateParts.add(updPrice);
        updateParts.add(updAmount);

        partsUpdSubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String[] oldPart = new String[2];
                oldPart[0] = (String) vendor.getSelectedItem();
                oldPart[1] = (String) partNumber.getSelectedItem();
                for(String e: oldPart){
                    if(e.equals("")){
                        return;
                    }
                        
                }
                
                String[]  newPart = new String[10];
                		//[0] =updPart.getText();
                		newPart[1] =updCore.getText();
                		newPart[2] =updInhead.getText();
                		newPart[3] =updOuthead.getText();
                		newPart[4] =updIncon.getText();
                		newPart[5] =updOucon.getText();
                		newPart[6] =updTmount.getText();
                		newPart[7] =updOilcool.getText();
                		newPart[8] =updPrice.getText();
                		newPart[9] =updAmount.getText();
                	
                Main.updatePart(oldPart, newPart);
                
            }
        });
        
        delpartsSubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String[] text = new String[2];
                text[0] = (String) vendor.getSelectedItem();
                text[1] = (String) partNumber.getSelectedItem();
                for(String e: text){
                    if(e.equals("")){
                        return;
                    }
                        
                }
                Main.deletePart(text);
                
            }
        });
  //      
        JPanel delbuttonPanel = new JPanel();
        delbuttonPanel.setLayout(new GridLayout(0,1));
        delbuttonPanel.add(partsUpdSubmit);
        updatePartsFrame.add(updateParts, BorderLayout.PAGE_START);
        updatePartsFrame.add(delbuttonPanel, BorderLayout.PAGE_END);
   
        //Browse by parts panel ///////////////////////////////////////////////
        
        
        JPanel browseByPart = new JPanel();
        browseByPart.setLayout(new BorderLayout());
        vendor.addItem(DEFAULT_LABELS[5]);
        partNumber.addItem(DEFAULT_LABELS[6]);
        browseByPartsBoxes.add(vendor);
        browseByPartsBoxes.add(partNumber);
        vendor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String choice = (String) ((JComboBox) event.getSource()).getSelectedItem();
                if (!choice.equals(DEFAULT_LABELS[5])) {
                    clearByPartsBoxes(1);
                    Main.send(choice, ScreenState.BROWSE_PART_MAKERS);
                }
            }
        });

        partNumber.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String choice = (String) ((JComboBox) event.getSource()).getSelectedItem();
                if (!choice.equals(DEFAULT_LABELS[6])) {
                    Main.send(choice, ScreenState.BROWSE_PARTS_FROM_MAKER);
                }
            }
        });

        JPanel boxPanel_p = new JPanel();
        boxPanel_p.add(vendor);
        boxPanel_p.add(partNumber);
        boxPanel_p.add(delpartsSubmit, BorderLayout.PAGE_END);
        browseByPart.add(boxPanel_p, BorderLayout.PAGE_START);
        browseByPart.add(updatePartsFrame, BorderLayout.PAGE_END);
        partsDisplay_p.setColumns(20);
        partsDisplay_p.setLineWrap(true);
        partsDisplay_p.setRows(5);
        partsDisplay_p.setWrapStyleWord(true);
        partsDisplay_p.setEditable(false);

        browseByPart.add(partsDisplay_p, BorderLayout.CENTER);
        tabs.addTab("Browse by Part", browseByPart);

        
        //create the insertParts tab
        JPanel insertPartsFrame = new JPanel();
        JPanel insertParts = new JPanel();

        insertPartsFrame.setLayout(new BorderLayout());
        insertParts.setLayout(new GridLayout(0, 11));
        insertParts.setSize(200, 200);
        final JTextField pnum = new JTextField();
        final JTextField pCore = new JTextField();
        final JTextField pInhead = new JTextField();
        final JTextField pOuthead = new JTextField();
        final JTextField pIncon = new JTextField();
        final JTextField pOucon= new JTextField();
        final JTextField pTmount = new JTextField();
        final JTextField pOilcool = new JTextField();
        final JTextField pPrice = new JTextField();
        final JTextField pAmount = new JTextField();
        JButton psubmit = new JButton("Submit");


        insertParts.add(new JLabel("Maker"));
        insertParts.add(new JLabel("PartNumber"));
        insertParts.add(new JLabel("Core"));
        insertParts.add(new JLabel("Inhead"));
        insertParts.add(new JLabel("Outhead"));
        insertParts.add(new JLabel("Incon"));
        insertParts.add(new JLabel("Oucon"));
        insertParts.add(new JLabel("TMount"));
        insertParts.add(new JLabel("OilCool"));
        insertParts.add(new JLabel("Price"));
        insertParts.add(new JLabel("Amount"));
        insertParts.add(partUpdateBox);
        insertParts.add(pnum);
        insertParts.add(pCore);
        insertParts.add(pInhead);
        insertParts.add(pOuthead);
        insertParts.add(pIncon);
        insertParts.add(pOucon);
        insertParts.add(pTmount);
        insertParts.add(pOilcool);
        insertParts.add(pPrice);
        insertParts.add(pAmount);
        psubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String[] text = new String[11];
                text[0] = (String) partUpdateBox.getSelectedItem();
                text[1] = pnum.getText();
                text[2] = pCore.getText();
                text[3] = pInhead.getText();
                text[4] = pOuthead.getText();
                text[5] = pIncon.getText();
                text[6] = pOucon.getText();
                text[7] = pTmount.getText();
                text[8] = pOilcool.getText();
                text[9] = pPrice.getText();
                text[10] = pAmount.getText();
                for(String e: text){
                    if(e.equals("")){
                        return;
                    }
                        
                }
               Main.insertPart(text);
                
            }
        });

        insertPartsFrame.add(insertParts, BorderLayout.PAGE_START);
        insertPartsFrame.add(psubmit, BorderLayout.PAGE_END);
        tabs.addTab("Insert Parts", insertPartsFrame);



        //create the insertCar tab
        JPanel insertCarsFrame = new JPanel();
        JPanel insertCars = new JPanel();

        insertCarsFrame.setLayout(new BorderLayout());
        insertCars.setLayout(new GridLayout(0, 7));
        insertCars.setSize(200, 200);
        final JButton csubmit = new JButton("Insert");
        final JTextField cmaker = new JTextField();
        final JTextField cmodel = new JTextField();
        final JTextField cyear = new JTextField();
        final JTextField cengine = new JTextField();
        final JTextField cDescription = new JTextField();
        final JTextField cLitres = new JTextField();
        final JTextField cCubic = new JTextField();
        insertCars.add(new JLabel("Make"));
        insertCars.add(new JLabel("Model"));
        insertCars.add(new JLabel("Year"));
        insertCars.add(new JLabel("Description"));
        insertCars.add(new JLabel("Litres"));
        insertCars.add(new JLabel("Engine Type"));
        insertCars.add(new JLabel("Cubic Inches"));
       // insertCars.add(new JLabel("Rlink"));
        insertCars.add(makeUpdateBox);
        insertCars.add(cmodel);
        insertCars.add(cyear);
        insertCars.add(cDescription);
        insertCars.add(cLitres);
        insertCars.add(cengine);
       insertCars.add(cCubic); 
        csubmit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String[] text = new String[7];
                text[0] = (String) makeUpdateBox.getSelectedItem();
                text[1] = cmodel.getText();
                text[2] = cyear.getText();
                text[3] = cDescription.getText();
                text[4] = cLitres.getText();
                text[5] = cengine.getText();
                text[6] = cCubic.getText();
              //  text[7] = cRlink.getText();
                for(String e: text){
                    if(e.equals("")){
                        return;
                    }
                        
                }
                Main.insertCar(text);
                
            }
        });

        insertCarsFrame.add(insertCars, BorderLayout.PAGE_START);
        insertCarsFrame.add(csubmit, BorderLayout.PAGE_END);
        tabs.addTab("Insert Cars", insertCarsFrame);


 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    /**
     * Clears the ComboBoxes in the ByMaker tab from a specified index.
     * @param from the index to clear from, inclusive.
     */
    public void clearByMakerBoxes(int from) {
        for (; from < browseByMakerBoxes.size(); from++) {
            browseByMakerBoxes.get(from).removeAll();
            browseByMakerBoxes.get(from).addItem(DEFAULT_LABELS[from]);
        }
        frame.repaint();
    }

    /**
     * Clears the ComboBoxes in the ByParts tab from a specified index.
     * @param from the index to clear from, inclusive.
     */
    public void clearByPartsBoxes(int from) {
        for (; from < browseByPartsBoxes.size(); from++) {
            browseByPartsBoxes.get(from).removeAll();
            browseByPartsBoxes.get(from).addItem(DEFAULT_LABELS[from + 5]);
        }
        frame.repaint();
    }

    /**
     * Sets the text for the large display box for the ByParts Tab
     * @param displayText the text to be displayed
     */
    public void setByPartsDisplay(String displayText) {
        System.out.println(displayText);
        partsDisplay_p.setText(displayText);
        frame.repaint();
    }

    /**
     * Sets the text for the large display box for the ByMaker Tab
     * @param displayText the text to be displayed
     */
    public void setByMakerDisplay(String displayText) {
        System.out.println(displayText);
        partsDisplay_m.setText(displayText);
        frame.repaint();

    }

    /**
     * Populates a given box with a given list from the By Maker tab
     * @param boxNumber the comboBox to populate
     * @param list the list to populate the comboBox with
     */
    public void populateByMakerBox(ScreenState state, String[] list) {
      
    	int boxNumber = screenStateToInt(state);
        if (boxNumber != -1) {
            for (String e : list) {
                if (e != null) {
                    browseByMakerBoxes.get(boxNumber).addItem(e);
                }
            }
        }

    }

    /**
     * Populates a given box with a given list from the By Parts tab
     * @param state the comboBox to populate
     * @param list the list to populate the comboBox with
     */
    public void populateByPartsBox(ScreenState state, String[] list) {
        int boxNumber = screenStateToIntParts(state);
        if (boxNumber != -1) {
            for (String e : list) {
                if (e != null) {
                    browseByPartsBoxes.get(boxNumber).addItem(e);
                }
            }
        }
    }

    private int screenStateToInt(ScreenState state) {
        switch (state) {
            case BROWSE_CAR_MAKERS:
                return 0;
            case BROWSE_MODELS:
                return 1;
            case BROWSE_YEARS:
                return 2;
            case BROWSE_ENGINE_TYPES:
                return 3;
            case BROWSE_PARTS_FOR_ENGINE:
                return 4;

            default:
                return -1;
        }
    }


    private int screenStateToIntParts(ScreenState state) {
        switch (state) {
            case BROWSE_PART_MAKERS:
                return 0;
            case BROWSE_PARTS_FROM_MAKER:
                return 1;

            default:
                return -1;
        }
    }
    
    //For Update tabs
    
    public static String[] carMaker = {
		"",
    	"BUICK",
		"CADILLAC",
		"CHEVROLET",
		"CHRYSLER",
		"FORD LIGHT TRUCK AND VAN",
		"FORD",
		"GMC TRUCK AND VAN",
		"INTERNATIONAL TRUCK (I.H.C.)",
		"ISUZU",
		"LINCOLN",
		"MAZDA",
		"OLDSMOBILE",
		"PORSCHE",
		"RENAULT",
		"SAAB",
		"SUBARU",
		"TOYOTA",
		"UPS",
		"VOLKSWAGEN"
	};
    
	static String[] partMaker = {
		"",
		"A.R.S.", 
		"BEHR",
		"DANIEL",
		"MODINE"
	};
    
    public void populateUpdateMake(){
    	for(String s : carMaker){
    		makeUpdateBox.addItem(s);
    	}
    }
    
    public void populateUpdatePart(){
    	for(String s : partMaker){
    		partUpdateBox.addItem(s);
    	}
    }
	public void printCarDetails(String[] columns) throws SQLException
	{
		String newLine = System.getProperty("line.separator");
		
		String carDetails = "Car Description" + newLine + "Model: " + columns[0] + newLine + "Year: " + columns[1] + newLine + 
				"Description: " + columns[2] + newLine  + "Litres: " + columns[3] + newLine + "Engine Type: " + columns[4] + newLine  +
				"Cubic Inches: " + columns[5];

		carDisplay.setText(carDetails);
		//frame.repaint();
	}
    
}
