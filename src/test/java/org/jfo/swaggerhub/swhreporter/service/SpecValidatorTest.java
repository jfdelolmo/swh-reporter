package org.jfo.swaggerhub.swhreporter.service;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
  void specValidatorTestValid(){
    Set<String> errors = specValidator.wrongReferences(validWrongReferenceData);
    Assertions.assertThat(errors).isEmpty();
  }

  @Test
  void specValidatorTestError(){
    Set<String> errors = specValidator.wrongReferences(invalidWrongReferenceData);
    Assertions.assertThat(errors).isNotEmpty().hasSize(1);
  }
  
  @Test
  void wrongOauthFlowValid(){
    Set<String> errors = specValidator.wrongOauthFlow(validOauthFlow);
    Assertions.assertThat(errors).isEmpty();
  }

  @Test
  void wrongOauthFlowError(){
    Set<String> errors = specValidator.wrongOauthFlow(invalidOauthFlow);
    Assertions.assertThat(errors).isNotEmpty().hasSize(1);
  }
}