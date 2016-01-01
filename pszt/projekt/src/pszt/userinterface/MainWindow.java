package pszt.userinterface;

import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import pszt.parser.*;
import pszt.algorithms.*;
import pszt.application.*;

/**
 * Main window of Resolving Machine programme
 *
 * @author Galfaroth7
 */
public class MainWindow extends JFrame
{

    private Parser parser;
    private ResolvingMachine resolver;
    private final JPanel mainPanel;
    private final JPanel upperPanel;
    private final JPanel lowerPanel;
    private final JPanel buttonPanel;
    private final JPanel optionPanel;
    private final JPanel clausesPanel;
    private final JPanel thesisPanel;

    private final ButtonGroup buttonGroup;
    private final JRadioButton shortClausesStrategyButton;
    private final JRadioButton lineStrategyButton;
    private final JButton runButton;
    private final JButton addNewClausesFieldButton;
    private final JButton addNewThesisFieldButton;

    private final List<JTextField> clausesFields = new ArrayList<>();
    private final List<JTextField> thesisFields = new ArrayList<>();

    private final List<String> clauses = new ArrayList<>();
    private final List<String> thesis = new ArrayList<>();
    private final JTextField thesisField;

    ActionListener addClause = new java.awt.event.ActionListener()
    {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt)
        {
            try
            {
                addNewClausesFieldAction(evt);
            } catch (IOException ex)
            {

            }
        }
    };

    ActionListener addThesis = new java.awt.event.ActionListener()
    {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt)
        {
            try
            {
                addNewThesisFieldAction(evt);
            } catch (IOException ex)
            {

            }
        }
    };

    ActionListener run = new java.awt.event.ActionListener()
    {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt)
        {
            try
            {
                runAction(evt);
            } catch (IOException ex)
            {

            }
        }
    };

    public MainWindow(Parser pr, ResolvingMachine rm)
    {
        parser = pr;
        resolver = rm;
        this.setTitle("Resolving Machine");
        setDefaultCloseOperation(MainWindow.EXIT_ON_CLOSE);

        Border emptyBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        Border etched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border compound = BorderFactory.createCompoundBorder(etched, emptyBorder);

        thesisField = new JTextField();

        shortClausesStrategyButton = new JRadioButton();
        lineStrategyButton = new JRadioButton();

        addNewClausesFieldButton = new JButton("Add new clause field");
        runButton = new JButton("Run");
        addNewThesisFieldButton = new JButton("Add new thesis field");

        runButton.setPreferredSize(addNewClausesFieldButton.getPreferredSize());

        JLabel shortClausesStrategyLabel = new JLabel("Short clauses strategy");
        JLabel lineStrategyLabel = new JLabel("Line strategy");

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setVisible(true);

        upperPanel = new JPanel();
        upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.PAGE_AXIS));
        upperPanel.setVisible(true);

        lowerPanel = new JPanel();
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.LINE_AXIS));
        lowerPanel.setVisible(true);

        mainPanel.add(upperPanel);
        mainPanel.add(lowerPanel);

        thesisPanel = new JPanel();
        thesisPanel.setLayout(new BoxLayout(thesisPanel, BoxLayout.PAGE_AXIS));
        thesisPanel.setVisible(true);
        thesisPanel.setBorder(compound);

        JScrollPane thesisScrollPane = new JScrollPane(thesisPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        thesisScrollPane.setVisible(true);

        lowerPanel.add(thesisScrollPane);

        clausesPanel = new JPanel();
        clausesPanel.setLayout(new BoxLayout(clausesPanel, BoxLayout.PAGE_AXIS));
        clausesPanel.setVisible(true);
        clausesPanel.setBorder(compound);

        JScrollPane clauseScrollPane = new JScrollPane(clausesPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        thesisScrollPane.setVisible(true);

        lowerPanel.add(clauseScrollPane);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setVisible(true);

        optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.LINE_AXIS));
        optionPanel.setVisible(true);
        upperPanel.add(optionPanel);
        upperPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        upperPanel.add(buttonPanel);

        buttonGroup = new ButtonGroup();

        buttonGroup.add(lineStrategyButton);
        buttonGroup.add(shortClausesStrategyButton);

        lineStrategyButton.setSelected(true);
        shortClausesStrategyButton.setSelected(false);

        optionPanel.add(lineStrategyButton);
        optionPanel.add(lineStrategyLabel);
        optionPanel.add(Box.createHorizontalGlue());
        optionPanel.add(addNewThesisFieldButton);
        optionPanel.add(addNewClausesFieldButton);

        buttonPanel.add(shortClausesStrategyButton);
        buttonPanel.add(shortClausesStrategyLabel);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(runButton);

        addNewClausesFieldButton.addActionListener(addClause);
        addNewThesisFieldButton.addActionListener(addThesis);
        runButton.addActionListener(run);

        lineStrategyButton.setVisible(true);
        shortClausesStrategyButton.setVisible(true);
        upperPanel.setBorder(compound);

        thesisPanel.add(new JLabel("Negated Thesis"));
        thesisPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        clausesPanel.add(new JLabel("Clauses"));
        clausesPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        this.setMinimumSize(new Dimension(550, 300));
        this.add(mainPanel);
        this.setVisible(true);

    }

    void addNewClausesFieldAction(java.awt.event.ActionEvent evt) throws IOException
    {
        JTextField textField = new JTextField();
        textField.setColumns(20);
        textField.setMaximumSize(textField.getPreferredSize());
        textField.setAlignmentX(LEFT_ALIGNMENT);

        clausesFields.add(textField);
        clausesPanel.add(textField);

        clausesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        clausesPanel.repaint();
        clausesPanel.revalidate();
    }

    void addNewThesisFieldAction(java.awt.event.ActionEvent evt) throws IOException
    {
        JTextField textField = new JTextField();
        textField.setColumns(20);
        textField.setMaximumSize(textField.getPreferredSize());
        textField.setAlignmentX(LEFT_ALIGNMENT);

        thesisFields.add(textField);
        thesisPanel.add(textField);

        thesisPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        thesisPanel.repaint();
        thesisPanel.revalidate();
    }

    void runAction(java.awt.event.ActionEvent evt) throws IOException
    {

        for (JTextField textField : clausesFields)
        {
            String s = textField.getText();
            if (s.length() > 0)
            {
                clauses.add(s);
            }
        }

        for (JTextField textField : thesisFields)
        {
            String s = textField.getText();
            if (s.length() > 0)
            {
                thesis.add(s);
            }

        }

        resolver.addKnowledgeBase(parser.parseClausesFromInterface(clauses));
        resolver.addTheses(parser.parseClausesFromInterface(thesis));
        List<ClauseWrapper> result = resolver.linearResolver(false);
        TreeWindow trwnd = new TreeWindow(result);
        try
        {
            PrintWriter out = new PrintWriter("resources/result1.out");
            for (ClauseWrapper node : result)
            {
                String text = node.toString();
                System.out.print(text);
                out.print(text);
            }
            out.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("Brak pliku" + e.getMessage());
            return;
        }

    }

//        for(String srt : clauses){
    //          System.out.println(srt);
    //    }
    /**
     * TODO Calling parser method (passing list of strings)
     */
}
