package com.lupa.kostka.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Counter implements Parcelable {

    private int value;
    private int valuesCount;


    public Counter() {
        this.value = 0;
        this.valuesCount = 0;
    }

    public synchronized void add(int value) {
        this.value += value;
        this.valuesCount ++;
    }

    public synchronized void clear() {
        value = 0;
        valuesCount = 0;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValuesCount() {
        return valuesCount;
    }

    public void setValuesCount(int valuesCount) {
        this.valuesCount = valuesCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.value);
        dest.writeInt(this.valuesCount);
    }

    protected Counter(Parcel in) {
        this.value = in.readInt();
        this.valuesCount = in.readInt();
    }

    public static final Parcelable.Creator<Counter> CREATOR = new Parcelable.Creator<Counter>() {
        @Override
        public Counter createFromParcel(Parcel source) {
            return new Counter(source);
        }

        @Override
        public Counter[] newArray(int size) {
            return new Counter[size];
        }
    };
}
