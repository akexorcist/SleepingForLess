package com.akexorcist.sleepingforless.view.settings;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Akexorcist on 3/22/2016 AD.
 */

@Parcel
public class OpenSourceLicense {
    @SerializedName("data")
    public List<License> licenseList;

    public List<License> getLicenseList() {
        return licenseList;
    }

    @Parcel
    public static class License {
        @SerializedName("library_name")
        public String libraryName;

        public String version;

        @SerializedName("project_name")
        public String projectName;

        @SerializedName("license_type")
        public String licenseType;

        @SerializedName("license_version")
        public String licenseVersion;

        public String getLibraryName() {
            return libraryName;
        }

        public String getVersion() {
            return version;
        }

        public String getProjectName() {
            return projectName;
        }

        public String getLicenseType() {
            return licenseType;
        }

        public String getLicenseVersion() {
            return licenseVersion;
        }
    }
}
