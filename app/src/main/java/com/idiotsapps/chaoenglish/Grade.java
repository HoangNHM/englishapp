package com.idiotsapps.chaoenglish;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class Grade implements Parcelable {
    private ArrayList<Unit> units = new ArrayList<Unit>();
    private float percent;
    private int grade;
    private int cIndexUnit = 0;
    private Date date;//last time user do the words of grade

    protected Grade(Parcel in) {
        percent = in.readFloat();
        grade = in.readInt();
        cIndexUnit = in.readInt();
    }

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };

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

    public float getPercent() {
        float sum = 0;
        for (int i = 0; i < units.size(); i++) {
            sum += units.get(i).getVocabPercent();
        }
        this.percent = sum/units.size(); //TODO: update percent for grammar and listen
        return this.percent;
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

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public Grade(int grade) {
        this.grade = grade;
        this.cIndexUnit = 0;

    }

    public Grade() {//

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(percent);
        dest.writeInt(grade);
        dest.writeInt(cIndexUnit);
    }
}
