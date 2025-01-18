package com.axe.steelCoils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CardNumberService {

        private final SteelCoilRepository steelCoilRepository;
        private final Logger logger = LoggerFactory.getLogger(CardNumberService.class);
        public CardNumberService(SteelCoilRepository steelCoilRepository) {
            this.steelCoilRepository = steelCoilRepository;
        }

        public String getLastCardNumberForSteelCategory(BigDecimal width) {
            return steelCoilRepository.getLastCardNumberForSteelCategory(width);
        }

        public String incrementLastDigit(String cardNumber) {
            logger.info("Incrementing last digit of card number: " + cardNumber);
            int i = cardNumber.length() - 1;
            while (i >= 0 && Character.isDigit(cardNumber.charAt(i))) {
                i--;
            }

            String prefix = cardNumber.substring(0, i + 1);
            String numericPart = cardNumber.substring(i + 1);

            int numericValue = Integer.parseInt(numericPart);
            numericValue++;

            // Use String.format to preserve leading zeros
            String incrementedPart = String.format("%0" + numericPart.length() + "d", numericValue);

            return prefix + incrementedPart;
        }

    public String changeCardNumber(String input) {
        // Define a regular expression pattern to match "KP" followed by digits and "-C"
        String regexPattern = "KP(\\d+)-C";

        // Use a regular expression matcher to find and replace the pattern
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);

        // Check if the pattern is found
        if (matcher.find()) {
            // Replace the matched pattern with "KP" followed by digits and "-O"
            return "KP" + matcher.group(1) + "-O";
        } else {
            // If the pattern is not found, add "-O" to the original string and return it
            return input + "-O";
        }
    }

}
