/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pszt.userinterface;





import java.awt.Dimension;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import pszt.parser.*;
import pszt.algorithms.*;
import pszt.application.*;

/**
 *
 * @author Misiek
 */
public class TreeWindow extends JFrame
{
    private final JPanel mainPanel;
    private JTree tree;
   
    TreeWindow(List<ClauseWrapper> result)
    {
        List<ClauseWrapper> treeBase = result;
        this.setTitle("Decision Tree");
        //setDefaultCloseOperation(TreeWindow.EXIT_ON_CLOSE);

        Border emptyBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        Border etched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border compound = BorderFactory.createCompoundBorder(etched, emptyBorder);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setVisible(true);
        
        JScrollPane scrollPane = new JScrollPane(null,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
        mainPanel.add(scrollPane);
        scrollPane.setVisible(true);
        
        this.setMinimumSize(new Dimension(550, 300));
        this.add(mainPanel);
        this.setVisible(true);
    }
}
