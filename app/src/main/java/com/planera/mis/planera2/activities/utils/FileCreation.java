package com.planera.mis.planera2.activities.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.planera.mis.planera2.activities.models.DataItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class FileCreation{
    public String doctorReport = " Doctor Report.xls";
    public String chemistReprot = " Chemist Report.xls";
    public String userReport = " User/MR Report.xls";
    public File file;

    public void exportReport(List<DataItem> reportList, File doc, Context context, int roleType){

        try {
            Date d = new Date();
            CharSequence s = DateFormat.format("MM-dd-yy hh:mm:ss", d.getTime());
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;


            if (roleType == AppConstants.ROLE_CHEMIST) {
                file = new File(doc, s.toString().replace(":", "-") + chemistReprot);
                    workbook = Workbook.createWorkbook(file, wbSettings);
                        //Excel sheet name. 0 represents first sheet
                        WritableSheet sheet = workbook.createSheet("Report", 0);
                        // column and row
                        sheet.addCell(new Label(0, 0, "Mr/User"));
                        sheet.addCell(new Label(1, 0, "Start Date"));
                        sheet.addCell(new Label(2, 0, "End Date"));
                        sheet.addCell(new Label(4, 0, "POB"));
                        for (int i = 1; i < reportList.size(); i++) {
                            if (!reportList.get(i).getChemistName().equals(null)) {
                                if (reportList.get(i).getIsBrand().equals("0")) {
                                    sheet.addCell(new Label(0, i, reportList.get(i).getChemistName()));
                                    sheet.addCell(new Label(1, i, reportList.get(i).getStartDate()));
                                    sheet.addCell(new Label(2, i, reportList.get(i).getEndDate()));
                                    sheet.addCell(new Label(3, i, reportList.get(i).getProductName() + "(" + reportList.get(i).getProductQty() + ")"));
                                }
                            }

                        }

                //closing cursor
                workbook.write();
                workbook.close();
                Toasty.success(context, "File Created Successfully", Toast.LENGTH_LONG).show();
            }

            if (roleType == AppConstants.ROLE_DOCTOR){
                file = new File(doc, s.toString().replace(":", "-")+ doctorReport);
                workbook = Workbook.createWorkbook(file, wbSettings);
                //Excel sheet name. 0 represents first sheet
                WritableSheet sheet = workbook.createSheet("Doctors Report", 0);
                // column and row
                sheet.addCell(new Label(0, 0, "Mr/User"));
                sheet.addCell(new Label(1, 0, "Start Date"));
                sheet.addCell(new Label(2, 0, "End Date"));
                sheet.addCell(new Label(3, 0, "Brand Interest"));
                sheet.addCell(new Label(4, 0, "Brand's Sample"));
                for (int i = 0; i < reportList.size(); i++) {
                    if (!reportList.get(i).getDoctorName().equals(null)) {
//                        if (reportList.get(i).getIsBrand().equals("1")) {
                            sheet.addCell(new Label(0, i, reportList.get(i).getDoctorName()));
                            sheet.addCell(new Label(1, i, reportList.get(i).getStartDate()));
                            sheet.addCell(new Label(2, i, reportList.get(i).getEndDate()));
                            sheet.addCell(new Label(3, i, reportList.get(i).getProductName()+"("+reportList.get(i).getInterestedLevel()));
                            if (reportList.get(i).getIsSample().equals("1")){
                                sheet.addCell(new Label(4, i, reportList.get(i).getProductName() + "(" + reportList.get(i).getProductQty() + ")"));
                            }

//                        }
                    }

                }

                //closing cursor
                workbook.write();
                workbook.close();
                Toasty.success(context, "File Created Successfully", Toast.LENGTH_LONG).show();


            }


            if (roleType == AppConstants.ROLE_USER){
                file = new File(doc, s.toString().replace(":", "-")+ userReport);
                workbook = Workbook.createWorkbook(file, wbSettings);
                //Excel sheet name. 0 represents first sheet
                WritableSheet sheet = workbook.createSheet("User's Report", 0);
                // column and row
                sheet.addCell(new Label(0, 0, "Chemist"));
                sheet.addCell(new Label(1, 0, "Doctor"));
                sheet.addCell(new Label(2, 0, "St. Date"));
                sheet.addCell(new Label(3, 0, "En. Date"));
                sheet.addCell(new Label(4, 0, "Gift"));
                sheet.addCell(new Label(5, 0, "POB"));
                sheet.addCell(new Label(6, 0, "Sample"));
                sheet.addCell(new Label(7, 0, "Brand Interest"));
                sheet.addCell(new Label(8, 0, "Is in Location"));


                for (int i = 1; i < reportList.size(); i++) {
                            if (reportList.get(i).getDoctorName().equals(null)){
                                sheet.addCell(new Label(0, i, reportList.get(i).getChemistName()));
                                if (!reportList.get(i).getGiftId().equals(null)){
                                    sheet.addCell(new Label(4, i, reportList.get(i).getGiftName()+"("+ reportList.get(i).getGiftQty()+")"));
                                }
                                if (reportList.get(i).getIsSample().equals("1")){
                                    sheet.addCell(new Label(6, i, reportList.get(i).getProductName()+"("+reportList.get(i).getProductQty()+")"));
                                }
                                sheet.addCell(new Label(7, i, reportList.get(i).getProductName()+"("+reportList.get(i).getInterestedLevel()));
                            }
                            if(reportList.get(i).getChemistName().equals(null)){
                                sheet.addCell(new Label(1, i, reportList.get(i).getDoctorName()));
                                if (reportList.get(i).getIsSample().equals("1")){
                                    sheet.addCell(new Label(5, i, reportList.get(i).getProductName() + "(" + reportList.get(i).getProductQty() + ")"));
                                }
                            }
                            sheet.addCell(new Label(2, i, reportList.get(i).getStartDate()));
                            sheet.addCell(new Label(3, i, reportList.get(i).getEndDate()));

                            sheet.addCell(new Label(8, i, reportList.get(i).getIsInLocation()));


                }

                //closing cursor
                workbook.write();
                workbook.close();
                Toasty.success(context, "File Created Successfully", Toast.LENGTH_LONG).show();
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
