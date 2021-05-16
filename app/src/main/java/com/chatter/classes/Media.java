package com.chatter.classes;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

@Entity
public class Media {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "mediaLink")
    public String mediaLink;
    @ColumnInfo(name = "mediaType")
    public int mediaType;//0 - photo
    @ColumnInfo(name = "data")
    public byte[] data;

    public Media(@NonNull String mediaLink, int mediaType, byte[] data){
        this.mediaLink = mediaLink;
        this.mediaType = mediaType;
        this.data = data;
    }
}
