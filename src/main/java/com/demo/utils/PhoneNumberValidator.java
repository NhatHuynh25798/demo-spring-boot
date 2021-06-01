package com.demo.utils;

import com.demo.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<Phone, String> {
    public static final Pattern MATCH_PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");
    public static final String[] VIETTEL_PHONE = {"096", "097", "098", "032", "033", "034", "035", "036", "037", "038", "039",};
    public static final String[] MOBI_PHONE = {"090", "093", "070", " 071", "072", "076", "078"};
    public static final String[] VINA_PHONE = {"091", "094", "083", "084", "085", "087", "089"};
    public static final String[] VIETNAM_MOBILE = {"092", "056", "058"};
    public static final String[] G_MOBILE = {"099"};
    public static final String[] SFONE = {"095"};

    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber == null || phoneNumber.trim().length() == 0) return false;
        String phoneNumberFormat = isValidString(phoneNumber);
        Matcher phoneNumberMatcher = MATCH_PHONE_PATTERN.matcher(phoneNumberFormat);
        return phoneNumberMatcher.matches();
    }

    public String isValidString(String phoneNumber) {
        return phoneNumber.replace(".", "").replace(" ", "").replace("-", "");
    }

    public boolean includes(String pattern, String[] array) {
        List<String> list = Arrays.asList(array);
        return list.contains(pattern);
    }

    public String detectPhoneNumber(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (!isValid(phoneNumber, constraintValidatorContext)) {
            return "Phone number is invalid!";
        }
        String startPhoneNumber = phoneNumber.substring(0, 3);
        System.out.println(startPhoneNumber);
        if (includes(startPhoneNumber, VIETTEL_PHONE)) {
            return "Viettel";
        }
        if (includes(startPhoneNumber, MOBI_PHONE)) {
            return "MobiPhone";
        }
        if (includes(startPhoneNumber, VINA_PHONE)) {
            return "VinaPhone";
        }
        if (includes(startPhoneNumber, VIETNAM_MOBILE)) {
            return "Vietnamobile";
        }
        if (includes(startPhoneNumber, G_MOBILE)) {
            return "GMobile";
        }
        if (includes(startPhoneNumber, SFONE)) {
            return "SFone";
        }
        return "Unidentified Carrier!";
    }
}
