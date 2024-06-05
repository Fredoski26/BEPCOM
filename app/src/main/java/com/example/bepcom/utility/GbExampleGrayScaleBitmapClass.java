package com.example.bepcom.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.greenbit.ansinistitl.GBANJavaWrapperDefinesANStruct;
import com.greenbit.ansinistitl.GBANJavaWrapperDefinesAnsinistVersions;
import com.greenbit.ansinistitl.GBANJavaWrapperDefinesCompressionAlgorithmsStrings;
import com.greenbit.ansinistitl.GBANJavaWrapperDefinesFingerPositions;
import com.greenbit.ansinistitl.GBANJavaWrapperDefinesImpressionCodes;
import com.greenbit.ansinistitl.GBANJavaWrapperDefinesRecordStruct;
import com.greenbit.ansinistitl.GBANJavaWrapperDefinesReturnCodes;
import com.greenbit.bozorth.BozorthJavaWrapperLibrary;
import com.greenbit.gbfir.GbfirJavaWrapperDefinesCompressionAlgorithm;
import com.greenbit.gbfir.GbfirJavaWrapperDefinesFingerPositions;
import com.greenbit.gbfir.GbfirJavaWrapperDefinesFirFormats;
import com.greenbit.gbfir.GbfirJavaWrapperDefinesImpressionTypes;
import com.greenbit.gbfir.GbfirJavaWrapperDefinesReturnCodes;
import com.greenbit.gbfir.GbfirJavaWrapperDefinesScaleUnits;
import com.greenbit.gbfrsw.GbfrswJavaWrapperDefinesCodingOptions;
import com.greenbit.gbfrsw.GbfrswJavaWrapperDefinesISOFMRFingerPositions;
import com.greenbit.gbfrsw.GbfrswJavaWrapperDefinesISOFMRFormat;
import com.greenbit.gbfrsw.GbfrswJavaWrapperDefinesISOFMRGBProprietaryData;
import com.greenbit.gbfrsw.GbfrswJavaWrapperDefinesRequestedOperations;
import com.greenbit.gbfrsw.GbfrswJavaWrapperDefinesReturnCodes;
import com.greenbit.gbfrsw.GbfrswJavaWrapperDefinesUnmatchedDataFactorInfluenceRecommended;
import com.greenbit.gbnfiq.GbNfiqJavaWrapperDefineMinutiaeDescriptor;
import com.greenbit.gbnfiq.GbNfiqJavaWrapperDefineReturnCodes;
import com.greenbit.gbnfiq.GbNfiqJavaWrapperLibrary;
import com.greenbit.gbnfiq2.GbNfiq2JavaWrapperDefineReturnCodes;
import com.greenbit.jpeg.GbjpegJavaWrapperDefinesReturnCodes;
import com.greenbit.lfs.LfsJavaWrapperDefinesMinutia;
import com.greenbit.lfs.LfsJavaWrapperLibrary;
import com.greenbit.usbPermission.IGreenbitLogger;
import com.greenbit.utils.GBJavaWrapperUtilByteArrayForJavaToCExchange;
import com.greenbit.utils.GBJavaWrapperUtilDoubleForJavaToCExchange;
import com.greenbit.utils.GBJavaWrapperUtilIntForJavaToCExchange;
import com.greenbit.wsq.WsqJavaWrapperDefinesReturnCodes;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import gamint.com.bippiistps470.models.storageFile;
import gamint.com.bippiistps470.models.storageFileImages;
import gamint.com.bippiistps470.utils.LfsJavaWrapperDefinesMinutiaN;

import static gamint.com.bippiistps470.BioMiniMain.ARGB2Gray;

public class GbExampleGrayScaleBitmapClass {

    //-------------------------------------------------------------
    // FIELDS
    //-------------------------------------------------------------
    public byte[] bytes;
    public int sx;
    public int sy;
    public boolean hasToBeSaved;
    public boolean isAcquisitionResult;


    byte[] T = null;

    //-------------------------------------------------------------
    // CONSTRUCTORS
    //-------------------------------------------------------------
    public GbExampleGrayScaleBitmapClass() {
        bytes = null;
        sx = sy = 0;
        isAcquisitionResult = false;
        hasToBeSaved = false;
    }

    public GbExampleGrayScaleBitmapClass(byte[] B, int width, int height, boolean save, boolean isAcqRes) {
        this.Build(B, width, height, save, isAcqRes);
    }

    public void Build(byte[] B, int width, int height, boolean save, boolean isAcqRes) {
        //act.LogAcquisitionPhaseOnScreen("Build: B.length = " + B.length + ", width = " + width + ", height = " + height);
        if (B.length < (width * height)) {
            //act.LogAcquisitionPhaseOnScreen("Build: B.length = " + B.length + ", less than width * height = :" + width + " * " + height + " = " + (width * height));
            return;
        }
        bytes = new byte[width * height];
        bytes = Arrays.copyOf(B, width * height);
        sx = width;
        sy = height;
        hasToBeSaved = save;
        isAcquisitionResult = isAcqRes;
    }

    public void Build(byte[] B, int width, int height) {
        this.Build(B, width, height, false, false);
    }

    //-------------------------------------------------------------
    // UTILITIES
    //-------------------------------------------------------------
    public Bitmap GetBmp() {
        if (bytes != null && sx > 0 && sy > 0) {
            Bitmap ValToRet;
            byte[] pixels = new byte[sx * sy * 4];

            for (int i = 0; i < bytes.length; i++) {
                pixels[i * 4] =
                        pixels[i * 4 + 1] =
                                pixels[i * 4 + 2] = bytes[i]; //Invert the source bits
                pixels[i * 4 + 3] = (byte) 0xff; // the alpha.
            }
            ValToRet = Bitmap.createBitmap(sx, sy, Bitmap.Config.ARGB_8888);
            ValToRet.copyPixelsFromBuffer(ByteBuffer.wrap(pixels));

            return ValToRet;
        }
        return null;
    }

    public void ClipImage(int ClipOrigX, int ClipOrigY, int ClipSx, int ClipSy) {
        if (ClipOrigX >= this.sx) return;
        if (ClipOrigY >= this.sy) return;
        if (ClipOrigX < 0 || ClipOrigY < 0 || ClipSx < 0 || ClipSy < 0) return;
        if (ClipSx > (this.sx - ClipOrigX)) ClipSx = (this.sx - ClipOrigX);
        if (ClipSy > (this.sy - ClipOrigY)) ClipSy = (this.sy - ClipOrigY);
        byte[] Clipped = new byte[ClipSx * ClipSy];
        int offsetSource = ClipOrigX + (ClipOrigY * this.sx);
        int offsetDestination = 0;
        for (int i = 0; i < ClipSy; i++) {
            System.arraycopy(this.bytes, offsetSource, Clipped, offsetDestination, ClipSx);
            offsetSource += this.sx;
            offsetDestination += ClipSx;
        }
        this.bytes = Clipped;
        this.sx = ClipSx;
        this.sy = ClipSy;
    }

    public static String GetGreenbitDirectoryName() {
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, "Greenbit");
        boolean success = true;
        if (!file.exists()) {
            success = file.mkdir();
        }
        path = file.getPath();
        return path;
    }

    public static ArrayList<String> GbBmpLoadListOfImages(String extensionVal, boolean includeExtensionInName) {
        File file = new File(GetGreenbitDirectoryName());
        String[] paths = file.list();
        ArrayList<String> ValToRet = new ArrayList<String>();

        for (String fname :
                paths) {
            String[] filenameArray = fname.split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            if (extension.equals(extensionVal)) {
                if (includeExtensionInName) ValToRet.add(fname);
                else ValToRet.add(filenameArray[filenameArray.length - 2]);
            }
        }

        return ValToRet;
    }


    //-------------------------------------------------------------
    // RAW
    //-------------------------------------------------------------

    public void SaveToRaw(String fileName, IGreenbitLogger act) {
        try {
            // Assume block needs to be inside a Try/Catch block.
            File file = new File(GetGreenbitDirectoryName(),
                    fileName + "_" + sx + "_" + sy + ".raw"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            fOut.write(bytes);
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveToRaw exception: " + e.getMessage());
        }
    }

    public static int[] GbBmpGetSizeFromRawFileName(String rawFName) {
        String[] filenameArray = rawFName.split("_");
        if (filenameArray.length >= 3) {
            String imgWS = filenameArray[filenameArray.length - 2], imgHS = filenameArray[filenameArray.length - 1];
            try {
                int[] imgSize = new int[2];
                imgSize[0] = Integer.parseInt(imgWS);
                imgSize[1] = Integer.parseInt(imgHS);
                return imgSize;
            } catch (NumberFormatException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static ArrayList<String> GbBmpLoadListOfRawImagesWithSize() {
        ArrayList<String> DummyList = GbBmpLoadListOfImages("raw", false),
                ValToRet = new ArrayList<String>();
        for (String item : DummyList) {
            if (GbBmpGetSizeFromRawFileName(item) != null) ValToRet.add(item);
        }
        return ValToRet;
    }

    public boolean GbBmpFromRawFileWithSize(String rawFName, IGreenbitLogger act) throws Exception {
        try {
            ////////////////////////////
            // Load file
            ////////////////////////////
            int[] imgSize = GbBmpGetSizeFromRawFileName(rawFName);
            if (imgSize == null)
                throw new Exception("GbBmpFromRawFileWithSize: file name does not contain size");
            File file = new File(GetGreenbitDirectoryName(),
                    rawFName + ".raw");
            InputStream fIn = null;
            fIn = new FileInputStream(file);
            byte[] rawStream = new byte[fIn.available()];
            fIn.read(rawStream);
            fIn.close(); // do not forget to close the stream
            this.Build(rawStream, imgSize[0], imgSize[1]);
            return true;
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            if (act != null) act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    //-------------------------------------------------------------
    // WSQ
    //-------------------------------------------------------------

    public void SaveToWsq(String fileName, IGreenbitLogger act) {
        try {
            // Assume block needs to be inside a Try/Catch block.
            GBJavaWrapperUtilIntForJavaToCExchange compressedFileSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            int RetVal;
            RetVal = GB_AcquisitionOptionsGlobals.WSQ_Jw.GetCompressedFileSize(
                    0.75, bytes, sx, sy, 8, 500, "MyComment", compressedFileSize
            );
            if (RetVal == WsqJavaWrapperDefinesReturnCodes.WSQPACK_FAIL) {
                throw new Exception("SaveToWsq WsqlibError: " + GB_AcquisitionOptionsGlobals.WSQ_Jw.GetLastErrorString());
            }
            byte[] WsqStream = new byte[compressedFileSize.Get()];
            RetVal = GB_AcquisitionOptionsGlobals.WSQ_Jw.Compress(WsqStream, 0.75, bytes, sx, sy, 8, 500, "MyComment");
            if (RetVal == WsqJavaWrapperDefinesReturnCodes.WSQPACK_FAIL) {
                throw new Exception("SaveToWsq WsqlibError: " + GB_AcquisitionOptionsGlobals.WSQ_Jw.GetLastErrorString());
            }
            File file = new File(GetGreenbitDirectoryName(),
                    fileName + ".wsq");
            act.LogOnScreen("Saving image as wsq; Storage dir: " + GetGreenbitDirectoryName() +
                    ", len = " + bytes.length);
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            fOut.write(WsqStream);
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveToWsq exception: " + e.getMessage());
        }
    }

    public boolean GBBmpFromWsqBuffer(byte[] wsqSource, boolean save, boolean isAcqRes, IGreenbitLogger act) throws Exception {
        try {
            ///////////////////////////////
            // convert from wsq to binary
            ///////////////////////////////
            GBJavaWrapperUtilIntForJavaToCExchange width = new GBJavaWrapperUtilIntForJavaToCExchange(),
                    height = new GBJavaWrapperUtilIntForJavaToCExchange(),
                    bitsPerPixel = new GBJavaWrapperUtilIntForJavaToCExchange(),
                    ppi = new GBJavaWrapperUtilIntForJavaToCExchange(),
                    lossy = new GBJavaWrapperUtilIntForJavaToCExchange();
            int RetVal = GB_AcquisitionOptionsGlobals.WSQ_Jw.GetUncompressedOutputParameters(
                    width, height, bitsPerPixel, ppi, lossy, wsqSource);
            if (RetVal == WsqJavaWrapperDefinesReturnCodes.WSQPACK_FAIL) {
                throw new Exception("GBBmpFromWsqBuffer " +
                        ", WsqlibError: " + GB_AcquisitionOptionsGlobals.WSQ_Jw.GetLastErrorString());
            }
            byte[] destination = new byte[width.Get() * height.Get() * (bitsPerPixel.Get() / 8)];
            RetVal = GB_AcquisitionOptionsGlobals.WSQ_Jw.Uncompress(destination,
                    width, height, bitsPerPixel, ppi, lossy, wsqSource);
            if (RetVal == WsqJavaWrapperDefinesReturnCodes.WSQPACK_FAIL) {
                throw new Exception("GBBmpFromWsqBuffer " +
                        ", WsqlibError: " + GB_AcquisitionOptionsGlobals.WSQ_Jw.GetLastErrorString());
            }
            this.Build(destination, width.Get(), height.Get(), save, isAcqRes);
            return true;
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    public boolean GbBmpFromWsqFile(String wsqFileName, boolean save, boolean isAcqRes, IGreenbitLogger act) throws Exception {
        try {
            ////////////////////////////
            // Load file
            ////////////////////////////
            if (act != null) act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    wsqFileName + ".wsq");
            InputStream fIn = null;
            fIn = new FileInputStream(file);
            byte[] wsqStream = new byte[fIn.available()];
            fIn.read(wsqStream);
            fIn.close(); // do not forget to close the stream
            String fInfo = GetGreenbitDirectoryName() + "/" + wsqFileName + " size: " + wsqStream.length;
            if (act != null) act.LogOnScreen(fInfo);
            ///////////////////////////////
            // convert from wsq to binary
            ///////////////////////////////
            return GBBmpFromWsqBuffer(wsqStream, save, isAcqRes, act);
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    public void SaveToGalleryEnroll(String fileName, IGreenbitLogger act) {
        try {
            // Assume block needs to be inside a Try/Catch block.
            act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    fileName + ".png"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);

            Bitmap pictureBitmap = this.GetBmp(); // obtaining the Bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            pictureBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            pictureBitmap.compress(Bitmap.CompressFormat.PNG, 85, baos); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate


            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

//            int imageFile = Integer.parseInt(fileName.substring(fileName.length()-1)) -1;
//            System.out.println(""+imageFile);
//            String fileImage = fileName.substring(0, fileName.length()-1) + imageFile;


//            String imageString = Base64.encodeToString(serializeImageFile(GetGreenbitDirectoryName() + "/" + fileImage + ".png"), Base64.DEFAULT);
            String imageString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            storageFileImages.fingerPrintImages.addFingerintsImages(imageString);
            Log.d("fingerprint", "Number of fingerprints images: " + storageFileImages.fingerPrintImages.getAllFingerprintsImages().size());


        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveToGallery" + e.getMessage());
        }
    }


    public static ArrayList<String> GbBmpLoadListOfWsqImages(boolean withExtension) {
        return GbBmpLoadListOfImages("wsq", withExtension);
    }

    //-------------------------------------------------------------
    // JPEG
    //-------------------------------------------------------------
    public void SaveToJpeg(String fileName, IGreenbitLogger act) {
        try {
            // Assume block needs to be inside a Try/Catch block.
            GBJavaWrapperUtilIntForJavaToCExchange compressedFileSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            int RetVal;
            RetVal = GB_AcquisitionOptionsGlobals.Jpeg_Jw.JpegGetParamsOfEncodedBuffer(
                    bytes, sx, sy, 8, 500, 100, compressedFileSize
            );
            if (RetVal != GbjpegJavaWrapperDefinesReturnCodes.GBJPEG_OK) {
                throw new Exception("SaveToJpeg JpeglibError: " + GB_AcquisitionOptionsGlobals.Jpeg_Jw.GetLastErrorString());
            }
            byte[] JpegStream = new byte[compressedFileSize.Get()];
            RetVal = GB_AcquisitionOptionsGlobals.Jpeg_Jw.JpegEncode(bytes, sx, sy, 8, 500, 100, JpegStream);
            if (RetVal != GbjpegJavaWrapperDefinesReturnCodes.GBJPEG_OK) {
                throw new Exception("SaveToJpeg " +
                        ", JpeglibError: " + GB_AcquisitionOptionsGlobals.Jpeg_Jw.GetLastErrorString());
            }
            File file = new File(GetGreenbitDirectoryName(),
                    fileName + ".jpeg");
            act.LogOnScreen("Saving image as jpeg; Storage dir: " + GetGreenbitDirectoryName() +
                    ", len = " + bytes.length);
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            fOut.write(JpegStream);
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveToJpeg exception: " + e.getMessage());
        }
    }

    public boolean GBBmpFromJpegBuffer(byte[] jpegSource, boolean save, boolean isAcqRes, IGreenbitLogger act) throws Exception {
        try {
            ///////////////////////////////
            // convert from wsq to binary
            ///////////////////////////////
            GBJavaWrapperUtilIntForJavaToCExchange width = new GBJavaWrapperUtilIntForJavaToCExchange(),
                    height = new GBJavaWrapperUtilIntForJavaToCExchange(),
                    bitsPerPixel = new GBJavaWrapperUtilIntForJavaToCExchange();
            int RetVal = GB_AcquisitionOptionsGlobals.Jpeg_Jw.JpegGetParametersOfDecodedBuffer(
                    jpegSource,
                    width, height, bitsPerPixel);
            if (RetVal != GbjpegJavaWrapperDefinesReturnCodes.GBJPEG_OK) {
                throw new Exception("GBBmpFromJpegBuffer " +
                        ", JpeglibError: " + GB_AcquisitionOptionsGlobals.Jpeg_Jw.GetLastErrorString());
            }
            byte[] destination = new byte[width.Get() * height.Get() * (bitsPerPixel.Get() / 8)];
            RetVal = GB_AcquisitionOptionsGlobals.Jpeg_Jw.JpegDecode(
                    jpegSource,
                    destination,
                    width, height, bitsPerPixel);
            if (RetVal != GbjpegJavaWrapperDefinesReturnCodes.GBJPEG_OK) {
                throw new Exception("GBBmpFromJpegBuffer " +
                        ", JpeglibError: " + GB_AcquisitionOptionsGlobals.Jpeg_Jw.GetLastErrorString());
            }
            this.Build(destination, width.Get(), height.Get(), save, isAcqRes);
            return true;
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    public boolean GbBmpFromJpegFile(String jpegFileName, boolean save, boolean isAcqRes, IGreenbitLogger act) throws Exception {
        try {
            ////////////////////////////
            // Load file
            ////////////////////////////
            act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    jpegFileName + ".jpeg");
            InputStream fIn = null;
            fIn = new FileInputStream(file);
            byte[] stream = new byte[fIn.available()];
            fIn.read(stream);
            fIn.close(); // do not forget to close the stream
            String fInfo = GetGreenbitDirectoryName() + "/" + jpegFileName + " size: " + stream.length;
            act.LogOnScreen(fInfo);
            ///////////////////////////////
            // convert from wsq to binary
            ///////////////////////////////
            return GBBmpFromJpegBuffer(stream, save, isAcqRes, act);
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    //-------------------------------------------------------------
    // JPEG2
    //-------------------------------------------------------------
    public void SaveToJpeg2(String fileName, IGreenbitLogger act) {
        try {
            // Assume block needs to be inside a Try/Catch block.
            GBJavaWrapperUtilIntForJavaToCExchange compressedFileSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            int RetVal;
            RetVal = GB_AcquisitionOptionsGlobals.Jpeg_Jw.Jp2GetParamsOfEncodedBuffer(
                    bytes, sx, sy, 8, 1, compressedFileSize
            );
            if (RetVal != GbjpegJavaWrapperDefinesReturnCodes.GBJPEG_OK) {
                throw new Exception("SaveToJpeg2 JpeglibError: " + GB_AcquisitionOptionsGlobals.Jpeg_Jw.GetLastErrorString());
            }
            byte[] Jp2Stream = new byte[compressedFileSize.Get()];
            RetVal = GB_AcquisitionOptionsGlobals.Jpeg_Jw.Jp2Encode(bytes, sx, sy, 8, 1, Jp2Stream);
            if (RetVal != GbjpegJavaWrapperDefinesReturnCodes.GBJPEG_OK) {
                throw new Exception("SaveToJpeg2 " +
                        ", JpeglibError: " + GB_AcquisitionOptionsGlobals.Jpeg_Jw.GetLastErrorString());
            }
            File file = new File(GetGreenbitDirectoryName(),
                    fileName + ".jp2");
            act.LogOnScreen("Saving image as jp2; Storage dir: " + GetGreenbitDirectoryName() +
                    ", len = " + bytes.length);
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            fOut.write(Jp2Stream);
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveToJpeg exception: " + e.getMessage());
        }
    }

    public boolean GBBmpFromJpeg2Buffer(byte[] jpeg2Source, boolean save, boolean isAcqRes, IGreenbitLogger act) throws Exception {
        try {
            ///////////////////////////////
            // convert from wsq to binary
            ///////////////////////////////
            GBJavaWrapperUtilIntForJavaToCExchange width = new GBJavaWrapperUtilIntForJavaToCExchange(),
                    height = new GBJavaWrapperUtilIntForJavaToCExchange(),
                    bitsPerPixel = new GBJavaWrapperUtilIntForJavaToCExchange();
            int RetVal = GB_AcquisitionOptionsGlobals.Jpeg_Jw.JpegGetParametersOfDecodedBuffer(
                    jpeg2Source,
                    width, height, bitsPerPixel);
            if (RetVal != GbjpegJavaWrapperDefinesReturnCodes.GBJPEG_OK) {
                throw new Exception("GBBmpFromJpegBuffer " +
                        ", JpeglibError: " + GB_AcquisitionOptionsGlobals.Jpeg_Jw.GetLastErrorString());
            }
            byte[] destination = new byte[width.Get() * height.Get() * (bitsPerPixel.Get() / 8)];
            RetVal = GB_AcquisitionOptionsGlobals.Jpeg_Jw.JpegDecode(
                    jpeg2Source,
                    destination,
                    width, height, bitsPerPixel);
            if (RetVal != GbjpegJavaWrapperDefinesReturnCodes.GBJPEG_OK) {
                throw new Exception("GBBmpFromJpegBuffer " +
                        ", JpeglibError: " + GB_AcquisitionOptionsGlobals.Jpeg_Jw.GetLastErrorString());
            }
            this.Build(destination, width.Get(), height.Get(), save, isAcqRes);
            return true;
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    public boolean GbBmpFromJpeg2File(String jpeg2FileName, boolean save, boolean isAcqRes, IGreenbitLogger act) throws Exception {
        try {
            ////////////////////////////
            // Load file
            ////////////////////////////
            act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    jpeg2FileName + ".jp2");
            InputStream fIn = null;
            fIn = new FileInputStream(file);
            byte[] stream = new byte[fIn.available()];
            fIn.read(stream);
            fIn.close(); // do not forget to close the stream
            String fInfo = GetGreenbitDirectoryName() + "/" + jpeg2FileName + " size: " + stream.length;
            act.LogOnScreen(fInfo);
            ///////////////////////////////
            // convert from wsq to binary
            ///////////////////////////////
            return GBBmpFromJpegBuffer(stream, save, isAcqRes, act);
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    public static ArrayList<String> GbBmpLoadListOfJp2Images(boolean withExtension) {
        return GbBmpLoadListOfImages("jp2", withExtension);
    }

    //-------------------------------------------------------------
    // DACTYMATCH
    //-------------------------------------------------------------
    private boolean InitGbfrswLibraryAndGetCodeSizeForCurrentBmp(
            GBJavaWrapperUtilIntForJavaToCExchange SampleCodeSize,
            GBJavaWrapperUtilIntForJavaToCExchange PackedSampleCodeSize,
            GBJavaWrapperUtilIntForJavaToCExchange TemplateCodeSize,
            GBJavaWrapperUtilIntForJavaToCExchange CompactTemplateCodeSize,
            IGreenbitLogger act
    ) {
        int RetVal;
        try {
            GBJavaWrapperUtilIntForJavaToCExchange VersionField1 = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange VersionField2 = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange VersionField3 = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange VersionField4 = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange MaxImageSizeX = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange MaxImageSizeY = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange MinImageSizeX = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange MinImageSizeY = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange MemoryBufferSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetVersionInfo(
                    GbfrswJavaWrapperDefinesRequestedOperations.GBFRSW_MEMORY_REQUEST_ALL,
                    VersionField1, VersionField2, VersionField3, VersionField4,
                    MaxImageSizeX, MaxImageSizeY, MinImageSizeX, MinImageSizeY,
                    MemoryBufferSize
            );
            System.out.println("InitGbfrsw: Mx = " + MaxImageSizeX.Get() +
                    ", My = " + MaxImageSizeY.Get() + ", mx = " + MinImageSizeX.Get() + ", my = " + MinImageSizeY.Get());
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib GetVersionInfo Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }
            // check image size
            if (this.sx > MaxImageSizeX.Get()) {
                throw new Exception("GbfrswlibError: current image size x is more than allowed, that is " + MaxImageSizeX.Get());
            }
            if (this.sy > MaxImageSizeY.Get()) {
                throw new Exception("GbfrswlibError: current image size y is more than allowed, that is " + MaxImageSizeY.Get());
            }
            if (this.sx < MinImageSizeX.Get()) {
                throw new Exception("GbfrswlibError: current image size x is less than minimum allowed, that is " + MinImageSizeX.Get());
            }
            if (this.sy < MinImageSizeY.Get()) {
                throw new Exception("GbfrswlibError: current image size y is less than minimum allowed, that is " + MinImageSizeY.Get());
            }
            // now get code max size
            RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetCodeMaxSize(this.sx, this.sy,
                    SampleCodeSize, PackedSampleCodeSize, TemplateCodeSize, CompactTemplateCodeSize);
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib GetCodeMaxSize Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("InitGbfrsw exception: " + e.getMessage());
            return false;
        }

        return true;
    }

    public void EncodeToTemplate(String fileName, int ImageFlags, IGreenbitLogger act) {
        try {
            /******************
             Get Code max size
             *****************/
            GBJavaWrapperUtilIntForJavaToCExchange SampleCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange PackedSampleCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange TemplateCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange CompactTemplateCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();

            if (!InitGbfrswLibraryAndGetCodeSizeForCurrentBmp(SampleCodeSize, PackedSampleCodeSize, TemplateCodeSize, CompactTemplateCodeSize, act)) {
                throw new Exception("EncodeToTemplate Error: InitGbfrswLibraryAndGetCodeSizeForCurrentBmp returned false");
            }
            /******************
             Encode
             *****************/
            byte[] SampleCode = new byte[SampleCodeSize.Get()];
            int RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.Coding(
                    this.sx, this.sy, bytes, ImageFlags,
                    GbfrswJavaWrapperDefinesCodingOptions.GBFRSW_OPTION_FINGERPRINT_PATTERN_CHECK,
                    SampleCode
            );
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib Coding Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }
            /***********************
             * Enroll
             **********************/
            byte[] TemplateCode = new byte[TemplateCodeSize.Get()];
            RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.Enroll(this.sx, this.sy,
                    SampleCode, TemplateCode);
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib Enroll Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }


            /*******************
             * Perform some operations
             ******************/
            //storageFile.fingerPrint.allFingerprints.add(Base64.encodeToString(TemplateCode, Base64.DEFAULT));
            String string = Base64.encodeToString(TemplateCode, Base64.DEFAULT);
//            byte[] Temp = Base64.decode(string, Base64.DEFAULT);

            storageFile.fingerPrint.addFingerints(string);

            /*******************
             * Save To File
             ******************/
            act.LogOnScreen("Saving image as gbfrsw template; Storage dir: " + GetGreenbitDirectoryName() +
                    ", len = " + bytes.length);

            File file = new File(GetGreenbitDirectoryName(),
                    fileName + ".gbfrsw");
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            fOut.write(TemplateCode);
//            fOut.write(Temp);
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("EncodeToTemplate exception: " + e.getMessage());
        }
    }

    public boolean MatchWithTemplate(String TemplateFName, int ImageFlagsForCoding,
                                     int UnmatchedDataFactor,
                                     boolean AddExtension, IGreenbitLogger act) {
        try {
            ////////////////////////////
            // Load file
            ////////////////////////////
            if (AddExtension) TemplateFName += ".gbfrsw";
            act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    TemplateFName);
            InputStream fIn = null;
            fIn = new FileInputStream(file);
            byte[] templateStream = new byte[fIn.available()];
            fIn.read(templateStream);
            fIn.close(); // do not forget to close the stream
            /////////////////////////////
            // ENCODE THIS
            /////////////////////////////
            /******************
             Get Code max size
             *****************/
            GBJavaWrapperUtilIntForJavaToCExchange SampleCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange PackedSampleCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange TemplateCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange CompactTemplateCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            if (!InitGbfrswLibraryAndGetCodeSizeForCurrentBmp(SampleCodeSize, PackedSampleCodeSize, TemplateCodeSize, CompactTemplateCodeSize, act)) {
                throw new Exception("EncodeToTemplate Error: InitGbfrswLibraryAndGetCodeSizeForCurrentBmp returned false");
            }
            /******************
             Encode
             *****************/
            byte[] SampleCode = new byte[SampleCodeSize.Get()];
            int RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.Coding(
                    this.sx, this.sy, bytes, ImageFlagsForCoding,
                    GbfrswJavaWrapperDefinesCodingOptions.GBFRSW_OPTION_FINGERPRINT_PATTERN_CHECK,
                    SampleCode
            );
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib Coding Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }
            ///////////////////////////////////
            // MATCH
            ///////////////////////////////////
            GBJavaWrapperUtilDoubleForJavaToCExchange MatchingScore = new GBJavaWrapperUtilDoubleForJavaToCExchange();
            RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.Match(SampleCode, templateStream,
                    GB_AcquisitionOptionsGlobals.SpeedVsPrecisionTradeoff,
                    UnmatchedDataFactor,
                    0, GB_AcquisitionOptionsGlobals.MatchRotationAngle, MatchingScore);

            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib Match Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }
            if (MatchingScore.Get() < GB_AcquisitionOptionsGlobals.MatchingThreshold) return false;
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveBinaryData exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void EncodeToLFSMinutiae(String fileName, int ImageFlags, IGreenbitLogger act) throws Exception {
        int RetVal;
        GB_AcquisitionOptionsGlobals.BOZORTH_Jw.Load();
        GB_AcquisitionOptionsGlobals.LFS_Jw.Load();

        GBJavaWrapperUtilIntForJavaToCExchange TemplateCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();

        /******
         Get minutiae
         *******/
        LfsJavaWrapperDefinesMinutiaN[] Probe = new LfsJavaWrapperDefinesMinutiaN[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];
        LfsJavaWrapperDefinesMinutia[] Probe1 = new LfsJavaWrapperDefinesMinutia[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];

        for (int i = 0; i < BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE; i++) {
            Probe[i] = new LfsJavaWrapperDefinesMinutiaN();
            Probe1[i] = new LfsJavaWrapperDefinesMinutia();
        }


        GBJavaWrapperUtilIntForJavaToCExchange MinutiaeNum = new GBJavaWrapperUtilIntForJavaToCExchange();

        try {
            RetVal = GB_AcquisitionOptionsGlobals.LFS_Jw.GetMinutiae(bytes, sx, sy, 8, 19.68, Probe1, MinutiaeNum);
            Log.d("bippiis", RetVal + " is return value");

            if (RetVal != LfsJavaWrapperLibrary.LFS_SUCCESS) {
                throw new Exception("EncodeToLfsMinutiae" +
                        ", EncodeToLfsMinutiae: " + GB_AcquisitionOptionsGlobals.LFS_Jw.GetLastErrorString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE; i++) {
            Probe[i].setXCoord(Probe1[i].getXCoord());
            Probe[i].setYCoord(Probe1[i].getYCoord());
            Probe[i].setReliability(Probe1[i].getReliability());
            Probe[i].setType(Probe1[i].getType());
            Probe[i].setDirection(Probe1[i].getDirection());
        }


        /*******
         * Perform some operations
         ******/
        //storageFile.fingerPrint.allFingerprints.add(Base64.encodeToString(TemplateCode, Base64.DEFAULT));
//        byte[] TemplateCode = new byte[TemplateCodeSize.Get()];
        //String string = Base64.encodeToString(TemplateCode, Base64.DEFAULT);
//            byte[] Temp = Base64.decode(string, Base64.DEFAULT);  //when I get back from server, I will use this to convert string to byte[]



        ////////////////////////////////////////////////////////////////////////////
        SaveToGalleryEnroll(fileName, act);

        ////////////////////////////////////////////////////////////////////////////

        String string = Base64.encodeToString(SerializeMinutiaeBuffer(Probe), Base64.DEFAULT);
        storageFile.fingerPrint.addFingerints(string);
        Log.d("fingerprint", "Number of fingerprints: " + storageFile.fingerPrint.getAllFingerprints().size());

        /*******
         * Save To File
         ******/
////        act.LogOnScreen("Saving image as LFS template; Storage dir: " + GetGreenbitDirectoryName() +
////                ", len = " + bytes.length);
//        File file = new File(GetGreenbitDirectoryName(),
//                fileName + ".lfs");
//        OutputStream fOut = new FileOutputStream(file);
//        //  Log.d("Fingerprint", "Probe size = " + Probe.length);
////        fOut.write(serializeMinutiaeBuffer(Probe)); // check this line out
//        // used for enrollment //   fOut.write(SerializeMinutiaeBuffer(Probe)); // check this line out
//
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
//            oos.writeObject(Probe);
//            TemplateCode = baos.toByteArray();
//            T = baos.toByteArray();
//
//        } catch (IOException e) {
//            // Error in serialization
//            e.printStackTrace();
//        }
//
//        fOut.write(TemplateCode); // check this line out
//        //   fOut.write(SerializeMinutiaeBuffer(Probe)); // check this line out
//        //     fOut.flush();
//        fOut.close(); // do not forget to close the stream


        File file = new File(GetGreenbitDirectoryName(),
                fileName + ".json");
        try {
            // Serialize Java object into JSON file.
//            mapper.writeValue(file, Probe);
            Gson gson = new Gson();
            String json = gson.toJson(Probe1);

            //           mapper.writeValue(file, json);
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            fw.write(json);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.d("Fingerprint", "Closed successfully");
    }

    public boolean Verify(IGreenbitLogger act) throws Exception {
        int RetVal;
        GB_AcquisitionOptionsGlobals.BOZORTH_Jw.Load();
        GB_AcquisitionOptionsGlobals.LFS_Jw.Load();

//        MinutiaeToBeVerified = GetMinutiae(CitizenFingerprint); //done
//        boolean found = false;
//        foreach (MinutiaeItem in CitizenStoredArray)
//        {
//            score = Compare(MinutiaeToBeVerified, MinutiaeItem);
//            if (score >= threshold) found = true;
//            if (found) break;
//        }
//        return found;


        File file = new File(GetGreenbitDirectoryName());
        String[] paths = file.list();

        /*******************
         * Getting Minutiae operations
         ******************/
        //from scanner


        LfsJavaWrapperDefinesMinutiaN[] Probe = new LfsJavaWrapperDefinesMinutiaN[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];
        LfsJavaWrapperDefinesMinutia[] Probe1 = new LfsJavaWrapperDefinesMinutia[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];

        for (int i = 0; i < BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE; i++) {
            Probe[i] = new LfsJavaWrapperDefinesMinutiaN();
            Probe1[i] = new LfsJavaWrapperDefinesMinutia();
        }

        GBJavaWrapperUtilIntForJavaToCExchange MinutiaeNum = new GBJavaWrapperUtilIntForJavaToCExchange();
        RetVal = GB_AcquisitionOptionsGlobals.LFS_Jw.GetMinutiae(bytes, sx, sy, 8, 19.68, Probe1, MinutiaeNum);
        Log.d("fingerprint", "Return Value = " + RetVal);

        for (String fname :
                paths) {

            String[] filenameArray = fname.split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            if (extension.equals("json")) {

                //   boolean res = MatchMinutiae(fname);

                if (RetVal != LfsJavaWrapperLibrary.LFS_SUCCESS) {
                    throw new Exception("DecodeFromLfsMinutiae" +
                            ": " + GB_AcquisitionOptionsGlobals.LFS_Jw.GetLastErrorString());
                }


                /*******************
                 * Matching operations
                 ******************/
                //from db

                //load file
                Log.d("fingerprint", "Current File: " + fname);
                //                act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
                File f = new File(GetGreenbitDirectoryName(),
                        fname);

                Gson gson = new Gson();
                JsonReader reader = new JsonReader(new FileReader(f.getAbsolutePath()));
                Log.d("path", "" + f.getAbsolutePath());
                Probe = gson.fromJson(reader, Probe.getClass()); // contains the whole reviews list

                LfsJavaWrapperDefinesMinutia[] Gallery = new LfsJavaWrapperDefinesMinutia[Probe.length];

                for (int i = 0; i < Probe1.length; i++) {
                    Gallery[i] = new LfsJavaWrapperDefinesMinutia();
                    Gallery[i].SetFields(Probe[i].getXCoord(),
                            Probe[i].getYCoord(),
                            Probe[i].getDirection(),
                            Probe[i].getReliability(),
                            Probe[i].getType());
                }


                String text = "\"minutiae_Probe\": [";

                for (int i = 0; i < Probe.length; i++) {
                    text += "\n" +
                            "XCoord: " + Probe[i].getXCoord() +
                            ", YCoord: " + Probe[i].getYCoord() +
                            ", Direction: " + Probe[i].getDirection() +
                            ", Reliability: " + Probe[i].getReliability() +
                            ", Type: " + Probe[i].getType() +
                            ", Direction: " + Probe[i].getDirection();
                }

                Log.d("json", text);


                Log.d("fingerprint", "Gallery Length = "
                        + Gallery.length + "\n MinutiaeNum Value " + MinutiaeNum.Value
                );


//                Log.d("fingerprint", "JSON from File: " + string + " JSON from Gallery: " + string1);

                // compare
                boolean found = false;
                GBJavaWrapperUtilIntForJavaToCExchange score = new GBJavaWrapperUtilIntForJavaToCExchange();
                RetVal = GB_AcquisitionOptionsGlobals.BOZORTH_Jw.bozDirectCall(
                        200, Gallery, MinutiaeNum.Value, Probe1, MinutiaeNum.Value,
                        score
                );
                if (RetVal != BozorthJavaWrapperLibrary.BOZORTH_NO_ERROR) {
                    // here error management...
                    throw new Exception("Verify LFS: Bozorth Library Exception");
                }
                Log.d("fingerprint", " Score: " + score.Get());
                if (score.Get() >= 24) // decision threshold to say whether the fingerprints match (0-500)
                    found = true;

                if (found) {
                    Log.d("fingerprint", "Found: " + found);

                    GB_AcquisitionOptionsGlobals.BOZORTH_Jw.Unload();
                    return true;
                }
            }
        }
        GB_AcquisitionOptionsGlobals.BOZORTH_Jw.Unload();
        return false;
    }

    public boolean Identify(int ImageFlagsForCoding, IGreenbitLogger act) {
        File file = new File(GetGreenbitDirectoryName());
        String[] paths = file.list();
        for (String fname :
                paths) {
            String[] filenameArray = fname.split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            if (extension.equals("gbfrsw")) {
                boolean res = MatchWithTemplate(fname, ImageFlagsForCoding,
                        GbfrswJavaWrapperDefinesUnmatchedDataFactorInfluenceRecommended.RECOMMENDED_FOR_IDENTIFICATION,
                        false, act);
                if (res) {
                    act.LogAsDialog("Identify found with file: " + fname);
                    return true;
                }
            }
        }
        act.LogAsDialog("Identify not found");
        return false;


    }

    /*
    *
             MinutiaeToBeVerified = GetMinutiae(CitizenFingerprint);
            boolean found = false;
            foreach (MinutiaeItem in CitizenStoredArray)
            {
                     score = Compare(MinutiaeToBeVerified, MinutiaeItem);
                     if (score >= threshold) found = true;
                     if (found) break;
            }
            return found;
    *
    * */


    public LfsJavaWrapperDefinesMinutiaN[] getMinutiaeFromDb(byte[] templateCode) {

        return deserialize(templateCode);
    }


    //-------------------------------------------------------------
    // PNG
    //-------------------------------------------------------------

    /**
     * @param fileName with no extension
     */
    public void SaveToGallery(String fileName, IGreenbitLogger act) {
        try {
            // Assume block needs to be inside a Try/Catch block.
            act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    fileName + ".png"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);

            Bitmap pictureBitmap = this.GetBmp(); // obtaining the Bitmap
            pictureBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveToGallery" + e.getMessage());
        }
    }

    public void SaveIntoAnsiNistFile(String fileName, IGreenbitLogger act, int ImageType) {
        try {
            GBANJavaWrapperDefinesANStruct AnsiNistStruct;
            AnsiNistStruct = new GBANJavaWrapperDefinesANStruct();
            int RetVal;
            /*****************************
             * create ansi nist struct
             */
            RetVal = GB_AcquisitionOptionsGlobals.AN2000_Jw.CreateAnsiNist(
                    AnsiNistStruct,
                    GBANJavaWrapperDefinesAnsinistVersions.VERSION_0300,
                    "TransType",
                    1,
                    "DestAgency",
                    "OrigAgency",
                    "TransContID0001",
                    "TransContRef0001",
                    90, 90,
                    "GreenbitDomain"
            );
            if (RetVal != GBANJavaWrapperDefinesReturnCodes.AN2K_DLL_NO_ERROR) {
                throw new Exception("CreateAnsiNist: " + GB_AcquisitionOptionsGlobals.AN2000_Jw.GetLastErrorString());
            }
            /*******************************
             * create type-14 record
             */
            GBANJavaWrapperDefinesRecordStruct Record = new GBANJavaWrapperDefinesRecordStruct();
            RetVal = GB_AcquisitionOptionsGlobals.AN2000_Jw.ImageToType14Record(
                    Record,
                    bytes, sx, sy,
                    19.5,
                    GBANJavaWrapperDefinesCompressionAlgorithmsStrings.COMP_NONE,
                    GBANJavaWrapperDefinesImpressionCodes.EFTS_14_LIVE_SCAN_PLAN,
                    "GreenBit Scanner", "No comment",
                    GBANJavaWrapperDefinesFingerPositions.EFTS_14_FGP_LEFT_INDEX,
                    null, null, null, null, null
            );
            if (RetVal != GBANJavaWrapperDefinesReturnCodes.AN2K_DLL_NO_ERROR) {
                GB_AcquisitionOptionsGlobals.AN2000_Jw.FreeAnsiNistAllocatedMemory(AnsiNistStruct);
                throw new Exception("ImageToType14Record: " + GB_AcquisitionOptionsGlobals.AN2000_Jw.GetLastErrorString());
            }
            /******************************************
             * insert record into struct
             */
            RetVal = GB_AcquisitionOptionsGlobals.AN2000_Jw.InsertRecordIntoAnsiNistStruct(AnsiNistStruct, Record, 1);
            if (RetVal != GBANJavaWrapperDefinesReturnCodes.AN2K_DLL_NO_ERROR) {
                GB_AcquisitionOptionsGlobals.AN2000_Jw.FreeAnsiNistAllocatedMemory(AnsiNistStruct);
                throw new Exception("InsertRecordIntoAnsiNistStruct: " + GB_AcquisitionOptionsGlobals.AN2000_Jw.GetLastErrorString());
            }
            /***********************************
             * Save into file
             */
            GBJavaWrapperUtilByteArrayForJavaToCExchange OutBuffer = new GBJavaWrapperUtilByteArrayForJavaToCExchange();
            RetVal = GB_AcquisitionOptionsGlobals.AN2000_Jw.WriteAnsiNistToBuffer(
                    AnsiNistStruct,
                    OutBuffer
            );
            GB_AcquisitionOptionsGlobals.AN2000_Jw.FreeAnsiNistAllocatedMemory(AnsiNistStruct);
            if (RetVal != GBANJavaWrapperDefinesReturnCodes.AN2K_DLL_NO_ERROR) {
                throw new Exception("WriteAnsiNistToBuffer: " + GB_AcquisitionOptionsGlobals.AN2000_Jw.GetLastErrorString());
            }
            GbExampleGrayScaleBitmapClass.SaveGenericBinaryFile(fileName,
                    act, "An2000", OutBuffer.Get());
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveIntoAnsiNistFile" + e.getMessage());
        }
    }

    public static void SaveGenericBinaryFile(String fileName, IGreenbitLogger act, String extension, byte[] BufferToSave) {
        try {
            // Assume block needs to be inside a Try/Catch block.
            act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    fileName + "." + extension);
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            fOut.write(BufferToSave);
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveGenericBinaryFile" + e.getMessage());
        }
    }

    //-------------------------------------------------------------
    // FIR
    //-------------------------------------------------------------
    public void SaveToFIR(String fileName, IGreenbitLogger act) {
        int RetVal;

        try {
            // Assume block needs to be inside a Try/Catch block.

            // Create FIR record
            RetVal = GB_AcquisitionOptionsGlobals.GBFIR_Jw.CreateFIR(GbfirJavaWrapperDefinesFirFormats.GBFIR_FORMAT_ISO,
                    31,
                    null,    // CBEFF Product ID
                    0,        // Capture Device ID
                    GbfirJavaWrapperDefinesScaleUnits.GBFIR_SU_PIXEL_PER_INCH,
                    500,    // ScanResolutionH
                    500,    // ScanResolutionV
                    500,    // ImageResolutionH
                    500,    // ImageResolutionV
                    8,        // PixelDepth
                    GbfirJavaWrapperDefinesCompressionAlgorithm.GBFIR_CA_UNCOMPRESSED_NO_BIT_PACKING);
            if (RetVal != GbfirJavaWrapperDefinesReturnCodes.GBFIR_RET_SUCCESS) {
                throw new Exception("SaveToFIR: " + GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetLastErrorString());
            }

            // add Image to record
            RetVal = GB_AcquisitionOptionsGlobals.GBFIR_Jw.AddImageToFIR(bytes, bytes.length, sx, sy,
                    1, 1, 100,
                    GbfirJavaWrapperDefinesFingerPositions.GBFIR_FP_UNKNOWN,
                    GbfirJavaWrapperDefinesImpressionTypes.GBFIR_IT_LIVE_SCAN_PLAIN);
            if (RetVal != GbfirJavaWrapperDefinesReturnCodes.GBFIR_RET_SUCCESS) {
                throw new Exception("SaveToFIR: " + GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetLastErrorString());
            }

            // write FIR to buffer
            int FIRLenght = GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetFIRLen();
            byte[] FIRBuffer = new byte[FIRLenght];
            RetVal = GB_AcquisitionOptionsGlobals.GBFIR_Jw.WriteFIRToBuffer(FIRBuffer);
            if (RetVal != GbfirJavaWrapperDefinesReturnCodes.GBFIR_RET_SUCCESS) {
                throw new Exception("SaveToFIR: " + GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetLastErrorString());
            }

            // save FIR buffer to file
            SaveGenericBinaryFile(fileName, act, "fir-iso", FIRBuffer);

            // Free internal resources
            GB_AcquisitionOptionsGlobals.GBFIR_Jw.FreeFIR();
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("SaveToFIR" + e.getMessage());
        }
    }

    public boolean GBBmpFromFIRBuffer(byte[] firBuffer, boolean save, boolean isAcqRes, IGreenbitLogger act) throws Exception {
        try {
            ///////////////////////////////
            // read FIR buffer
            ///////////////////////////////
            int RetVal = GB_AcquisitionOptionsGlobals.GBFIR_Jw.ReadFIRFromBuffer(
                    firBuffer, firBuffer.length, GbfirJavaWrapperDefinesFirFormats.GBFIR_FORMAT_ISO);
            if (RetVal != GbfirJavaWrapperDefinesReturnCodes.GBFIR_RET_SUCCESS) {
                throw new Exception("GBBmpFromFIRBuffer " +
                        ", ReadFIRFromBuffer: " + GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetLastErrorString());
            }

            ///////////////////////////////
            // read header fields
            ///////////////////////////////
            GBJavaWrapperUtilIntForJavaToCExchange ImageAcquisitionLevel = new GBJavaWrapperUtilIntForJavaToCExchange();
            byte[] CBEFFProductId = new byte[4];
            GBJavaWrapperUtilIntForJavaToCExchange CaptureDeviceID = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange ScaleUnits = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange ScanResolutionH = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange ScanResolutionV = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange ImageResolutionH = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange ImageResolutionV = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange PixelDepth = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange ImageCompressionAlgorithm = new GBJavaWrapperUtilIntForJavaToCExchange();

            RetVal = GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetFIRHeaderFields(ImageAcquisitionLevel,
                    CBEFFProductId, CaptureDeviceID,
                    ScaleUnits, ScanResolutionH,
                    ScanResolutionV, ImageResolutionH,
                    ImageResolutionV, PixelDepth,
                    ImageCompressionAlgorithm);
            if (RetVal != GbfirJavaWrapperDefinesReturnCodes.GBFIR_RET_SUCCESS) {
                throw new Exception("GBBmpFromFIRBuffer " +
                        ", GetFIRHeaderFields: " + GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetLastErrorString());
            }

            ///////////////////////////////
            // get number of fingers
            ///////////////////////////////
            int NumFingers = GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetNumberOfFingers();

            if (NumFingers != 0) {
                // load only the first one
                int Index = 0;
                GBJavaWrapperUtilIntForJavaToCExchange CountView = new GBJavaWrapperUtilIntForJavaToCExchange();
                GBJavaWrapperUtilIntForJavaToCExchange ViewNumber = new GBJavaWrapperUtilIntForJavaToCExchange();
                GBJavaWrapperUtilIntForJavaToCExchange ImageQuality = new GBJavaWrapperUtilIntForJavaToCExchange();
                GBJavaWrapperUtilIntForJavaToCExchange FingerPosition = new GBJavaWrapperUtilIntForJavaToCExchange();
                GBJavaWrapperUtilIntForJavaToCExchange ImpressionType = new GBJavaWrapperUtilIntForJavaToCExchange();
                RetVal = GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetFingerRecordHeaderFields(Index, CountView,
                        ViewNumber, ImageQuality, FingerPosition, ImpressionType);
                if (RetVal != GbfirJavaWrapperDefinesReturnCodes.GBFIR_RET_SUCCESS) {
                    throw new Exception("GBBmpFromFIRBuffer " +
                            ", GetFingerRecordHeaderFields: " + GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetLastErrorString());
                }

                // get image size
                GBJavaWrapperUtilIntForJavaToCExchange ImageBufferSize = new GBJavaWrapperUtilIntForJavaToCExchange();
                RetVal = GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetFingerImageSize(Index, ImageBufferSize);
                if (RetVal != GbfirJavaWrapperDefinesReturnCodes.GBFIR_RET_SUCCESS) {
                    throw new Exception("GBBmpFromFIRBuffer " +
                            ", GetFingerImageSize: " + GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetLastErrorString());
                }

                // allocate image buffer
                byte[] ImageBuffer = new byte[ImageBufferSize.Get() * (PixelDepth.Get() / 8)];

                // get image buffer
                GBJavaWrapperUtilIntForJavaToCExchange ImageWidth = new GBJavaWrapperUtilIntForJavaToCExchange();
                GBJavaWrapperUtilIntForJavaToCExchange ImageHeight = new GBJavaWrapperUtilIntForJavaToCExchange();
                RetVal = GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetFingerImage(Index, ImageBuffer,
                        ImageWidth, ImageHeight);
                if (RetVal != GbfirJavaWrapperDefinesReturnCodes.GBFIR_RET_SUCCESS) {
                    throw new Exception("GBBmpFromFIRBuffer " +
                            ", GetFingerImage: " + GB_AcquisitionOptionsGlobals.GBFIR_Jw.GetLastErrorString());
                }

                // build the bitmap
                this.Build(ImageBuffer, ImageWidth.Get(), ImageHeight.Get(), save, isAcqRes);
            }

            // Free internal resources
            GB_AcquisitionOptionsGlobals.GBFIR_Jw.FreeFIR();

            return true;
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    public boolean GbBmpFromFIRFile(String FIRFileName, boolean save, boolean isAcqRes, IGreenbitLogger act) throws Exception {
        try {
            ////////////////////////////
            // Load file
            ////////////////////////////
            if (act != null) act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    FIRFileName + ".fir");
            InputStream fIn = null;
            fIn = new FileInputStream(file);
            byte[] firStream = new byte[fIn.available()];
            fIn.read(firStream);
            fIn.close(); // do not forget to close the stream
            String fInfo = GetGreenbitDirectoryName() + "/" + FIRFileName + " size: " + firStream.length;
            if (act != null) act.LogOnScreen(fInfo);
            ///////////////////////////////
            // convert from wsq to binary
            ///////////////////////////////
            return GBBmpFromFIRBuffer(firStream, save, isAcqRes, act);
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    // NFIQ
    public void GetNFIQQuality(IGreenbitLogger act) {
        try {
            GBJavaWrapperUtilIntForJavaToCExchange V1 = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange V2 = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange V3 = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange V4 = new GBJavaWrapperUtilIntForJavaToCExchange();

            int RetVal = GB_AcquisitionOptionsGlobals.GBNFIQ_Jw.GetVersion(V1, V2, V3, V4);

            GBJavaWrapperUtilIntForJavaToCExchange NFIQQuality = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange MinutiaeNumber = new GBJavaWrapperUtilIntForJavaToCExchange();

            // if needed, allocate minutiae array; otherwise, pass null
            GbNfiqJavaWrapperDefineMinutiaeDescriptor[] MinutiaeList = new GbNfiqJavaWrapperDefineMinutiaeDescriptor[GbNfiqJavaWrapperLibrary.GBNFIQ_MAX_MINUTIAE];
            for (int i = 0; i < GbNfiqJavaWrapperLibrary.GBNFIQ_MAX_MINUTIAE; i++)
                MinutiaeList[i] = new GbNfiqJavaWrapperDefineMinutiaeDescriptor();

            RetVal = GB_AcquisitionOptionsGlobals.GBNFIQ_Jw.GetImageQuality(bytes, sx, sy, NFIQQuality, MinutiaeNumber, MinutiaeList);

            if (RetVal != GbNfiqJavaWrapperDefineReturnCodes.GBNFIQ_NO_ERROR) {
                throw new Exception("GetNFIQQuality " +
                        ", GetImageQuality: " + GB_AcquisitionOptionsGlobals.GBNFIQ_Jw.GetLastErrorString());
            }
            //act.LogOnScreen("NFIQ RetVal: " + RetVal );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // NFIQ2
    public void GetNFIQ2Quality(IGreenbitLogger act) {
        try {

            int RetVal;
            Log.d("NFIQ2", "Inside GetNFIQ2Quality");


            GBJavaWrapperUtilIntForJavaToCExchange NFIQ2Quality = new GBJavaWrapperUtilIntForJavaToCExchange();

            RetVal = GB_AcquisitionOptionsGlobals.GBNFIQ2_Jw.GetImageQuality(bytes, sx, sy, NFIQ2Quality);

            if (RetVal != GbNfiq2JavaWrapperDefineReturnCodes.GBNFIQ2_NO_ERROR) {
                throw new Exception("GetNFIQ2Quality " +
                        ", GetImagefQuality: " + GB_AcquisitionOptionsGlobals.GBNFIQ2_Jw.GetLastErrorString());
            }
            //act.LogOnScreen("NFIQ2: " + NFIQ2Quality.Value + " fine->RetVal: " + RetVal );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lfs Bozorth
    public boolean TestLfsBozorth(String fileName) {
        try {

            int RetVal;

            GBJavaWrapperUtilIntForJavaToCExchange TemplateCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();

            LfsJavaWrapperDefinesMinutia[] Probe = new LfsJavaWrapperDefinesMinutia[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];

//            LfsJavaWrapperDefinesMinutiaN[] myProbe = new LfsJavaWrapperDefinesMinutiaN[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];


            for (int i = 0; i < BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE; i++) {
                Probe[i] = new LfsJavaWrapperDefinesMinutia();
                //  myProbe[i] = new LfsJavaWrapperDefinesMinutiaN();

            }

            GBJavaWrapperUtilIntForJavaToCExchange MinutiaeNum = new GBJavaWrapperUtilIntForJavaToCExchange();

            RetVal = GB_AcquisitionOptionsGlobals.LFS_Jw.GetMinutiae(bytes, sx, sy, 8, 19.68, Probe, MinutiaeNum);

            if (RetVal != LfsJavaWrapperLibrary.LFS_SUCCESS) {

                throw new Exception("TestLfs" +
                        ", TestLfsBozorth: " + GB_AcquisitionOptionsGlobals.LFS_Jw.GetLastErrorString());
            }

            GBJavaWrapperUtilIntForJavaToCExchange score = new GBJavaWrapperUtilIntForJavaToCExchange();

            RetVal = GB_AcquisitionOptionsGlobals.BOZORTH_Jw.bozDirectCall(200, Probe, MinutiaeNum.Value, Probe, MinutiaeNum.Value, score);

            if (RetVal != BozorthJavaWrapperLibrary.BOZORTH_NO_ERROR) {
                throw new Exception("TestBozorth" +
                        ", TestLfsBozorth: " + GB_AcquisitionOptionsGlobals.BOZORTH_Jw.GetLastErrorString());
                //        return false;
            }

            Log.d("fingerprint", "TestLfsBozorth: " + score.Value + " fine->RetVal: " + RetVal);


            /*******************
             * Perform some operations and save to file
             ******************/
            byte[] TemplateCode = new byte[TemplateCodeSize.Get()];
            storageFile.fingerPrint.allFingerprints.add(Base64.encodeToString(TemplateCode, Base64.DEFAULT));

            //            byte[] Temp = Base64.decode(string, Base64.DEFAULT);  //when I get back from server, I will use this to convert string to byte[]

            File file = new File(GetGreenbitDirectoryName(),
                    fileName + ".lfs");
            OutputStream fOut = new FileOutputStream(file);

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(Probe);
                TemplateCode = baos.toByteArray();
                T = baos.toByteArray();

            } catch (IOException e) {
                // Error in serialization
                e.printStackTrace();
            }

            fOut.write(TemplateCode); // check this line out
            fOut.close(); // do not forget to close the stream


        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    public void EncodeToAnsi378Template(String fileName, int ImageFlags, IGreenbitLogger act) {
        try {
            /******************
             Get Code max size
             *****************/
            GBJavaWrapperUtilIntForJavaToCExchange SampleCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange PackedSampleCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange TemplateCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            GBJavaWrapperUtilIntForJavaToCExchange CompactTemplateCodeSize = new GBJavaWrapperUtilIntForJavaToCExchange();
            if (!InitGbfrswLibraryAndGetCodeSizeForCurrentBmp(SampleCodeSize, PackedSampleCodeSize, TemplateCodeSize, CompactTemplateCodeSize, act)) {
                throw new Exception("EncodeToTemplate Error: InitGbfrswLibraryAndGetCodeSizeForCurrentBmp returned false");
            }
            /******************
             Encode
             *****************/
            byte[] SampleCode = new byte[SampleCodeSize.Get()];
            int RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.Coding(
                    this.sx, this.sy, bytes, ImageFlags,
                    GbfrswJavaWrapperDefinesCodingOptions.GBFRSW_OPTION_FINGERPRINT_PATTERN_CHECK,
                    SampleCode
            );
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib Coding Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }
            /***********************
             * Enroll
             **********************/
            byte[] TemplateCode = new byte[TemplateCodeSize.Get()];
            RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.Enroll(this.sx, this.sy,
                    SampleCode, TemplateCode);
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib Enroll Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }
            /***********************
             * Convert
             **********************/
            GBJavaWrapperUtilIntForJavaToCExchange MaxFRMLength = new GBJavaWrapperUtilIntForJavaToCExchange();
            RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.ISOGetMaxFMRLength(
                    this.sx, this.sy,
                    GbfrswJavaWrapperDefinesISOFMRFormat.GBFRSW_ISO_FMR_FORMAT_INCITS,
                    GbfrswJavaWrapperDefinesISOFMRGBProprietaryData.GBFRSW_ISO_FMR_GB_PROPR_DATA_NONE,
                    MaxFRMLength);
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib ISOGetMaxFMRLength Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }

            byte[] FMRIsoBuffer = new byte[(MaxFRMLength.Get())];
            GBJavaWrapperUtilIntForJavaToCExchange FMRBufferLen = new GBJavaWrapperUtilIntForJavaToCExchange();
            RetVal = GB_AcquisitionOptionsGlobals.GBFRSW_Jw.ISOGBTemplateToFMR(
                    // INPUT
                    TemplateCode,
                    this.sx, this.sy,
                    GbfrswJavaWrapperDefinesISOFMRFingerPositions.GBFRSW_ISO_FMR_FINGER_POS_UNKNOWN,
                    GbfrswJavaWrapperDefinesISOFMRFormat.GBFRSW_ISO_FMR_FORMAT_INCITS,
                    GbfrswJavaWrapperDefinesISOFMRGBProprietaryData.GBFRSW_ISO_FMR_GB_PROPR_DATA_NONE,
                    // OUTPUT
                    FMRIsoBuffer,
                    FMRBufferLen
            );
            if (RetVal == GbfrswJavaWrapperDefinesReturnCodes.GBFRSW_ERROR) {
                throw new Exception("Gbfrswlib ISOGBTemplateToFMR Error : " + GB_AcquisitionOptionsGlobals.GBFRSW_Jw.GetLastErrorString());
            }
            /*******************
             * Save To File
             ******************/
            act.LogOnScreen("Saving image as ANSI 378 template; Storage dir: " + GetGreenbitDirectoryName() +
                    ", len = " + bytes.length);
            File file = new File(GetGreenbitDirectoryName(),
                    fileName + ".ansi378");
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            fOut.write(FMRIsoBuffer);
            fOut.close(); // do not forget to close the stream
        } catch (Exception e) {
            e.printStackTrace();
            act.LogAsDialog("EncodeToAnsi378Template exception: " + e.getMessage());
        }
    }

//    public void EncodeToLFSMinutiae(String fileName, int ImageFlags, IGreenbitLogger act) {
//        try {
//            int RetVal;
//
//            /******************
//             Get minutiae
//             *****************/
//            LfsJavaWrapperDefinesMinutia[] Probe = new LfsJavaWrapperDefinesMinutia[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];
//
//            for (int i = 0; i < BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE; i++)
//                Probe[i] = new LfsJavaWrapperDefinesMinutia();
//
//            GBJavaWrapperUtilIntForJavaToCExchange MinutiaeNum = new GBJavaWrapperUtilIntForJavaToCExchange();
//
//            RetVal = GB_AcquisitionOptionsGlobals.LFS_Jw.GetMinutiae(bytes, sx, sy, 8, 19.68, Probe, MinutiaeNum);
//
//            if (RetVal != LfsJavaWrapperLibrary.LFS_SUCCESS) {
//                throw new Exception("EncodeToLfsMinutiae" +
//                        ", EncodeToLfsMinutiae: " + GB_AcquisitionOptionsGlobals.LFS_Jw.GetLastErrorString());
//            }
//
//
//            /*******************
//             * Save To File
//             ******************/
//            act.LogOnScreen("Saving image as ANSI 378 template; Storage dir: " + GetGreenbitDirectoryName() +
//                    ", len = " + bytes.length);
//            File file = new File(GetGreenbitDirectoryName(),
//                    fileName + ".lfs");
//            OutputStream fOut = new FileOutputStream(file);
//            Log.d("Fingerprint", "Probe size = " + Probe.length);
//            fOut.write(SerializeMinutiaeBuffer(Probe)); // check this line out
//            fOut.close(); // do not forget to close the stream
//
//            Log.d("Fingerprint", "Closed successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//            act.LogAsDialog("EncodeToLFSMinutiae exception: " + e.getMessage());
//        }
//    }

    private byte[] serializeMinutiaeBuffer(LfsJavaWrapperDefinesMinutia[] MinutiaeArrayToSerialize) {
        byte[] serializedMBuffer = null;
        //       byte[] serializedMBuffer = new byte[0]; // what I used during enrollment
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutputStream out = new ObjectOutputStream(bos);

            for (LfsJavaWrapperDefinesMinutia lfsJavaWrapperDefinesMinutia : MinutiaeArrayToSerialize) {

                out.writeObject(lfsJavaWrapperDefinesMinutia);
            }
            serializedMBuffer = bos.toByteArray();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializedMBuffer;
    }

    private byte[] SerializeMinutiaeBuffer(LfsJavaWrapperDefinesMinutiaN[] MinutiaeArrayToSerialize) {

        return SerializationUtils.serialize(MinutiaeArrayToSerialize);
    }

    private LfsJavaWrapperDefinesMinutiaN[] deserialize(byte[] templateCode) {
        return SerializationUtils.deserialize(templateCode);
    }

    public static String GetBippiisDirectoryName() {
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, "Greenbit");
        if (!file.exists()) {
            file.mkdir();
        }
        path = file.getPath();
        return path;
    }


    public boolean GbBmpFromBmpFile(String bmpFileName, boolean save, boolean isAcqRes) throws Exception {
        try {
            ////////////////////////////
            // Load file
            ////////////////////////////
            //  act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
            File file = new File(GetGreenbitDirectoryName(),
                    bmpFileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
            byte[] destination = ARGB2Gray(bitmap);
            this.Build(destination, bitmap.getWidth(), bitmap.getHeight(), save, isAcqRes);
            ///////////////////////////////
            // convert from bmp to binary
            ///////////////////////////////
            return true;
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            //      act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    public boolean GbBmpFromBmp(Bitmap bmp, boolean save, boolean isAcqRes) throws Exception {
        try {
            ////////////////////////////
            // Load file
            ////////////////////////////
            //  act.LogOnScreen("Storage dir: " + GetGreenbitDirectoryName());
//            File file = new File(GetGreenbitDirectoryName(),
//                    bmpFileName + ".bmp");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //           Bitmap bitmap = BitmapFactory.decodeFile(bmp, options);
//            Bitmap bitmap1 = BitmapFactory.decodeFile(bmp, options);
//            bmp.setConfig(options);
            byte[] destination = ARGB2Gray(bmp);
            this.Build(destination, bmp.getWidth(), bmp.getHeight(), save, isAcqRes);
            ///////////////////////////////
            // convert from bmp to binary
            ///////////////////////////////
            return true;
        } catch (Exception e) {
            byte[] whiteImage = {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            this.Build(whiteImage, 2, 2, false, false);
            e.printStackTrace();
            //      act.LogAsDialog(e.getMessage());
            return false;
        }
    }

    // Lfs Bozorth
    public boolean TestLfsBozorth1() {
        boolean returnable = true;
        try {
            GB_AcquisitionOptionsGlobals.BOZORTH_Jw.Load();
            GB_AcquisitionOptionsGlobals.LFS_Jw.Load();

            int RetVal;
            /******************
             Get minutiae
             *****************/
            LfsJavaWrapperDefinesMinutia[] Probe1 = new LfsJavaWrapperDefinesMinutia[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];
            LfsJavaWrapperDefinesMinutiaN[] Probe = new LfsJavaWrapperDefinesMinutiaN[BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE];
            for (int i = 0; i < BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE; i++) {
                Probe1[i] = new LfsJavaWrapperDefinesMinutia();
                Probe[i] = new LfsJavaWrapperDefinesMinutiaN();
            }

            GBJavaWrapperUtilIntForJavaToCExchange MinutiaeNum = new GBJavaWrapperUtilIntForJavaToCExchange();

            RetVal = GB_AcquisitionOptionsGlobals.LFS_Jw.GetMinutiae(bytes, sx, sy, 8, 19.68, Probe1, MinutiaeNum);

            if (RetVal != LfsJavaWrapperLibrary.LFS_SUCCESS) {
                Log.d("fingerprint", "not worked: RetVal = " + RetVal);
                returnable = false;
                throw new Exception("TestLfs" +
                        ", TestLfsBozorth: " + GB_AcquisitionOptionsGlobals.LFS_Jw.GetLastErrorString());
            } else {
                Log.d("fingerprint", "Worked perfectly");
                returnable = true;

            }

            GBJavaWrapperUtilIntForJavaToCExchange score = new GBJavaWrapperUtilIntForJavaToCExchange();

            RetVal = GB_AcquisitionOptionsGlobals.BOZORTH_Jw.bozDirectCall(200, Probe1, MinutiaeNum.Value, Probe1, MinutiaeNum.Value, score);

            if (RetVal != BozorthJavaWrapperLibrary.BOZORTH_NO_ERROR) {
                Log.d("fingerprint", " not worked: RetVal = " + RetVal);
                returnable = false;
                throw new Exception("TestBozorth" +
                        ", TestLfsBozorth: " + GB_AcquisitionOptionsGlobals.BOZORTH_Jw.GetLastErrorString());
            } else {
                Log.d("fingerprint", "Worked perfectly");

                returnable = true;
            }
            Log.d("fingerprint", "end");
            //       act.LogOnScreen("TestLfsBozorth: " + score.Value + " fine->RetVal: " + RetVal);

            for (int i = 0; i < BozorthJavaWrapperLibrary.BOZORTH_MAX_MINUTIAE; i++) {
                Probe[i].setXCoord(Probe1[i].getXCoord());
                Probe[i].setYCoord(Probe1[i].getYCoord());
                Probe[i].setReliability(Probe1[i].getReliability());
                Probe[i].setType(Probe1[i].getType());
                Probe[i].setDirection(Probe1[i].getDirection());
            }


            /*******************
             * Perform some operations
             ******************/
            String string = Base64.encodeToString(SerializeMinutiaeBuffer(Probe), Base64.DEFAULT);
            storageFile.fingerPrint.addFingerints(string);
            // Log.d("fingerprint", "Number of fingerprints: " + storageFile.fingerPrint.getAllFingerprints().size() + "fingerprint Val: " + string);


        } catch (Exception e) {
            e.printStackTrace();
            returnable = false;
        }
        return returnable;
    }


}