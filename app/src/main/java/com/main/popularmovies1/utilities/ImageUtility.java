package com.main.popularmovies1.utilities;

import com.main.popularmovies1.common.Constants;

public class ImageUtility {
    public static String generateImageUrl(String posterPath){
        return Constants.IMAGE_BASE_URL +
                Constants.IMAGE_BASE_SIZE +
                posterPath;
    }
}
