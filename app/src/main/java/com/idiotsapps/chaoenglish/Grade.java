package com.idiotsapps.chaoenglish;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class Grade {
    private ArrayList<Unit> units = new ArrayList<Unit>();
    private int percent;
    private int grade;
    private int cIndexUnit = 0;
    private Date date;//last time user do the words of grade

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public int getcIndexUnit() {
        return cIndexUnit;
    }

    public Unit getCurrentUnit() {
        return units.get(cIndexUnit);
    }
    public Unit getNextUnit() {
        if (cIndexUnit < (units.size() -1)) {
            cIndexUnit++;
            return units.get(cIndexUnit);
        }else
            return null;
    }
    public boolean isLastUnit(){
        if(cIndexUnit == (units.size() - 1)){
            return true;
        }else{
            return false;
        }
    }


    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public Grade(int grade, int percent) {
        this.grade = grade;
        this.percent = percent;
        this.cIndexUnit = 0;

    }

    public Grade() {//

    }
}
