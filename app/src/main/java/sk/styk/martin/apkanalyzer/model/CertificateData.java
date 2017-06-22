package sk.styk.martin.apkanalyzer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;
import java.util.Date;

/**
 * Represents data obtained from certificate file
 * <p>
 * Created by Martin Styk on 22.06.2017.
 */
public class CertificateData implements Parcelable {
    private String signAlgorithm;
    private Date startDate;
    private Date endDate;
    private String publicKeyMd5;
    private String certMd5;
    private BigInteger serialNumber;
    private String issuerName;
    private String issuerOrganization;
    private String issuerCountry;
    private String subjectName;
    private String subjectOrganization;
    private String subjectCountry;

    public String getSignAlgorithm() {
        return signAlgorithm;
    }

    public void setSignAlgorithm(String signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPublicKeyMd5() {
        return publicKeyMd5;
    }

    public void setPublicKeyMd5(String publicKeyMd5) {
        this.publicKeyMd5 = publicKeyMd5;
    }

    public String getCertMd5() {
        return certMd5;
    }

    public void setCertMd5(String certMd5) {
        this.certMd5 = certMd5;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getIssuerOrganization() {
        return issuerOrganization;
    }

    public void setIssuerOrganization(String issuerOrganization) {
        this.issuerOrganization = issuerOrganization;
    }

    public String getIssuerCountry() {
        return issuerCountry;
    }

    public void setIssuerCountry(String issuerCountry) {
        this.issuerCountry = issuerCountry;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectOrganization() {
        return subjectOrganization;
    }

    public void setSubjectOrganization(String subjectOrganization) {
        this.subjectOrganization = subjectOrganization;
    }

    public String getSubjectCountry() {
        return subjectCountry;
    }

    public void setSubjectCountry(String subjectCountry) {
        this.subjectCountry = subjectCountry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificateData data = (CertificateData) o;

        if (signAlgorithm != null ? !signAlgorithm.equals(data.signAlgorithm) : data.signAlgorithm != null)
            return false;
        if (startDate != null ? !startDate.equals(data.startDate) : data.startDate != null)
            return false;
        if (endDate != null ? !endDate.equals(data.endDate) : data.endDate != null) return false;
        if (publicKeyMd5 != null ? !publicKeyMd5.equals(data.publicKeyMd5) : data.publicKeyMd5 != null)
            return false;
        if (certMd5 != null ? !certMd5.equals(data.certMd5) : data.certMd5 != null) return false;
        if (serialNumber != null ? !serialNumber.equals(data.serialNumber) : data.serialNumber != null)
            return false;
        if (issuerName != null ? !issuerName.equals(data.issuerName) : data.issuerName != null)
            return false;
        if (issuerOrganization != null ? !issuerOrganization.equals(data.issuerOrganization) : data.issuerOrganization != null)
            return false;
        if (issuerCountry != null ? !issuerCountry.equals(data.issuerCountry) : data.issuerCountry != null)
            return false;
        if (subjectName != null ? !subjectName.equals(data.subjectName) : data.subjectName != null)
            return false;
        if (subjectOrganization != null ? !subjectOrganization.equals(data.subjectOrganization) : data.subjectOrganization != null)
            return false;
        return subjectCountry != null ? subjectCountry.equals(data.subjectCountry) : data.subjectCountry == null;

    }

    @Override
    public int hashCode() {
        int result = signAlgorithm != null ? signAlgorithm.hashCode() : 0;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (publicKeyMd5 != null ? publicKeyMd5.hashCode() : 0);
        result = 31 * result + (certMd5 != null ? certMd5.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (issuerName != null ? issuerName.hashCode() : 0);
        result = 31 * result + (issuerOrganization != null ? issuerOrganization.hashCode() : 0);
        result = 31 * result + (issuerCountry != null ? issuerCountry.hashCode() : 0);
        result = 31 * result + (subjectName != null ? subjectName.hashCode() : 0);
        result = 31 * result + (subjectOrganization != null ? subjectOrganization.hashCode() : 0);
        result = 31 * result + (subjectCountry != null ? subjectCountry.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CertificateData{" +
                "signAlgorithm='" + signAlgorithm + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", publicKeyMd5='" + publicKeyMd5 + '\'' +
                ", certMd5='" + certMd5 + '\'' +
                ", serialNumber=" + serialNumber +
                ", issuerName='" + issuerName + '\'' +
                ", issuerOrganization='" + issuerOrganization + '\'' +
                ", issuerCountry='" + issuerCountry + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", subjectOrganization='" + subjectOrganization + '\'' +
                ", subjectCountry='" + subjectCountry + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.signAlgorithm);
        dest.writeLong(this.startDate != null ? this.startDate.getTime() : -1);
        dest.writeLong(this.endDate != null ? this.endDate.getTime() : -1);
        dest.writeString(this.publicKeyMd5);
        dest.writeString(this.certMd5);
        dest.writeSerializable(this.serialNumber);
        dest.writeString(this.issuerName);
        dest.writeString(this.issuerOrganization);
        dest.writeString(this.issuerCountry);
        dest.writeString(this.subjectName);
        dest.writeString(this.subjectOrganization);
        dest.writeString(this.subjectCountry);
    }

    public CertificateData() {
    }

    protected CertificateData(Parcel in) {
        this.signAlgorithm = in.readString();
        long tmpStartDate = in.readLong();
        this.startDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        this.publicKeyMd5 = in.readString();
        this.certMd5 = in.readString();
        this.serialNumber = (BigInteger) in.readSerializable();
        this.issuerName = in.readString();
        this.issuerOrganization = in.readString();
        this.issuerCountry = in.readString();
        this.subjectName = in.readString();
        this.subjectOrganization = in.readString();
        this.subjectCountry = in.readString();
    }

    public static final Creator<CertificateData> CREATOR = new Creator<CertificateData>() {
        @Override
        public CertificateData createFromParcel(Parcel source) {
            return new CertificateData(source);
        }

        @Override
        public CertificateData[] newArray(int size) {
            return new CertificateData[size];
        }
    };
}

