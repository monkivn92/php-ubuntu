package com.vuongpv.swan;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import org.w3c.tidy.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SwanController
{
    private Stage stage, modalStage;

    private DBHelper db;

    @FXML
    TextField txt_section_id ;

    @FXML
    TextField txt_sub_section_id;

    @FXML
    Button btn_save;

    @FXML
    Button btn_view_html;

    @FXML
    Button btn_load_item;

    @FXML
    Button btn_load_db;

    @FXML
    HTMLEditor html_editor;

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    @FXML
    public void LoadDB()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        File selectedFile = fileChooser.showOpenDialog(stage);
        String targetPath = selectedFile.getAbsolutePath();
        if(!targetPath.isEmpty())
        {
            this.db = new DBHelper(targetPath);
        }

        if(this.db != null)
        {
            Alert("Load DB successfully!");
        }

    }

    @FXML
    public void SaveToDatabase()
    {
        if(this.db == null)
        {
            Alert("DB is not found");
            return;
        }
        String sec_id = txt_section_id.getText();
        String subsec_id = txt_sub_section_id.getText();
        String content = html_editor.getHtmlText();
        String query = "";
        PreparedStatement prestmt;
        try
        {
            ResultSet rs = this.db.execute(
                    "select * from swan_sections " +
                            "where section_id="+sec_id+" AND subsection_id="+subsec_id
            );

            if (rs != null && rs.next())
            {
                query = "UPDATE swan_sections SET content=? " +
                        "WHERE section_id="+sec_id+" AND subsection_id="+subsec_id;
                prestmt = this.db.getPrepareStatement(query);
                prestmt.setString(1, content);
                prestmt.executeUpdate();
                Alert("Update successfully");
            }
            else
            {
                query = "INSERT INTO swan_sections(section_id, subsection_id, content) " +
                        "VALUES (?,?,?) ";
                prestmt = this.db.getPrepareStatement(query);
                prestmt.setInt(1, Integer.valueOf(sec_id));
                prestmt.setInt(2, Integer.valueOf(subsec_id));
                prestmt.setString(3, content);
                prestmt.executeUpdate();
                Alert("New item is added successfully");
            }
        }
        catch (Exception e)
        {
            Alert("Savetodatabase function: "+e.getMessage());
        }

    }

    @FXML
    public void ViewHtmlCode()
    {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(modalStage);

        Pane dialogBox = new Pane();
        dialogBox.setPrefHeight(600);
        dialogBox.setPrefWidth(600);

        TextArea text_box = new TextArea();
        text_box.setPrefHeight(600);
        text_box.setPrefWidth(600);

        text_box.setWrapText(true);
        text_box.setFont(new Font(14));
        String formatted_html = "";
        try
        {
            formatted_html = FormatHTML(html_editor.getHtmlText());

        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        text_box.setText(formatted_html);

        dialogBox.getChildren().add(text_box);

        Scene dialogScene = new Scene(dialogBox, 600, 600);

        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    public void LoadSavedItem()
    {
        String sec_id = txt_section_id.getText();
        String subsec_id = txt_sub_section_id.getText();

        if(this.db == null)
        {
            Alert("DB is not found");
            return;
        }

        if(sec_id.isEmpty() && subsec_id.isEmpty())
        {
            return;
        }

        ResultSet rs = this.db.execute(
                "select * from swan_sections where section_id="+sec_id+" AND subsection_id="+subsec_id
        );

        try
        {

            while (rs.next())
            {
                html_editor.setHtmlText(rs.getString("content"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String FormatHTML(String data) throws UnsupportedEncodingException
    {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");

        tidy.setWraplen(Integer.MAX_VALUE);
        tidy.setPrintBodyOnly(true);
        tidy.setXmlOut(true);
        tidy.setSmartIndent(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString("UTF-8");
    }

    private void Alert(String msg)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(modalStage);

        Pane dialogBox = new Pane();
        dialogBox.setPrefHeight(150);
        dialogBox.setPrefWidth(300);

        Label lb = new Label(msg);

        dialogBox.getChildren().add(lb);
        Scene dialogScene = new Scene(dialogBox, 300, 150);

        dialog.setScene(dialogScene);
        dialog.show();
    }

}
