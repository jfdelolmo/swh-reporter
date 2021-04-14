package org.jfo.swaggerhub.swhreporter.service;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.validator.SpecValidator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("When SpecValidator is used")
class SpecValidatorTest {
  
  private static final String validWrongReferenceData = 
      "      properties:\n" +
      "        id:\n" +
      "          $ref: 'https://api.swaggerhub.com/domains/CREALOGIX/CommonDefinitions/3.2.0#/components/schemas/Identifier'\n";

  private static final String invalidWrongReferenceData =
      "      properties:\n" +
          "        id:\n" +
          "          $ref: './CommonDefinitions.yaml#/components/schemas/Identifier'\n";

  private static final String validOauthFlow = 
      "components:\n" + 
      "   securitySchemes:\n" +
      "       ClxApiOAuth2:\n" +
      "         type: oauth2\n" +
      "         flows:\n" +
      "           authorizationCode:\n" +
      "             authorizationUrl: https://api.crealogix.com/oauth2/authorization\n" +
      "             tokenUrl: https://api.crealogix.com/oauth2/token\n" +
      "             scopes:\n" +
      "               https://api.crealogix.com/payments/payment.write: Create, modify and delete privilege to all payments types (no signing)\n" +
      "               https://api.crealogix.com/payments/payment.read: Read payment data and status";

  private static final String invalidOauthFlow = "";
  
  private final SpecValidator specValidator = new SpecValidator();
  
  @Test
  @DisplayName("Should pass without errors")
  void specValidatorTestValid(){
    Set<String> errors = specValidator.wrongReferences(validWrongReferenceData);
    Assertions.assertThat(errors).isEmpty();
  }

  @Test
  @DisplayName("Should pass with 1 error")
  void specValidatorTestError(){
    Set<String> errors = specValidator.wrongReferences(invalidWrongReferenceData);
    Assertions.assertThat(errors).isNotEmpty().hasSize(1);
  }
  
  @Test
  @DisplayName("Should pass as the specification contains a valid SecuritySchema definition")
  void wrongOauthFlowValid(){
    Set<String> errors = specValidator.wrongOauthFlow(validOauthFlow);
    Assertions.assertThat(errors).isEmpty();
  }

  @Test
  @DisplayName("Should pass with an error because does not contain the valid ClxApiOAuth2 definition")
  void wrongOauthFlowError(){
    Set<String> errors = specValidator.wrongOauthFlow(invalidOauthFlow);
    Assertions.assertThat(errors).isNotEmpty().hasSize(1);
  }
}