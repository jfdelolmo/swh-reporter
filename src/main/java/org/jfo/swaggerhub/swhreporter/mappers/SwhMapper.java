package org.jfo.swaggerhub.swhreporter.mappers;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.time.OffsetDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.model.db.Api;
import org.jfo.swaggerhub.swhreporter.model.db.Domain;
import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewMember;
import org.jfo.swaggerhub.swhreporter.model.db.NewOpenApiDocument;
import org.jfo.swaggerhub.swhreporter.model.db.NewProperties;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.db.NewTeam;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonProperty;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.CollaborationMembership;
import org.jfo.swaggerhub.swhreporter.model.swh.CollaborationTeamMembership;
import org.jfo.swaggerhub.swhreporter.model.swh.Project;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

@Component
public class SwhMapper {

  private static final int MAX_DESCRIPTION_LENGTH = 10;

  public NewSpecification apisJsonApiToSpecModel(ApisJsonApi apisJsonApi) {
    NewSpecification specification = new NewSpecification();

    specification.setName(apisJsonApi.getName());
    int minSize = Math.min(MAX_DESCRIPTION_LENGTH, apisJsonApi.getDescription().length());
    specification.setDescription(StringUtils.isEmpty(apisJsonApi.getDescription()) ?
        "No description provided" :
        apisJsonApi.getDescription().substring(0, minSize) + "..."
    );

    NewProperties p = mapToProperties(apisJsonApi.getProperties());
    specification.setProperties(p);

    return specification;
  }

  public NewProperties mapToProperties(List<ApisJsonProperty> input) {
    NewProperties properties = new NewProperties();
    input.forEach(p -> {
      if ("Swagger".equalsIgnoreCase(p.getType())) {
        properties.setType("API");
        properties.setUrl(p.getUrl());
      }
      if ("X-Domain".equalsIgnoreCase(p.getType())) {
        properties.setType("DOMAIN");
        properties.setUrl(p.getUrl());
      }
      if ("X-Version".equalsIgnoreCase(p.getType())) {
        properties.setVersion(p.getValue());
      }
      if ("X-Created".equalsIgnoreCase(p.getType())) {
        properties.setCreated(OffsetDateTime.parse(p.getValue()));
      }
      if ("X-Created".equalsIgnoreCase(p.getType())) {
        properties.setModified(OffsetDateTime.parse(p.getValue()));
      }
    });
    return properties;
  }

  public NewCollaboration collaborationSwhToModel(Collaboration input) {
    NewCollaboration output = new NewCollaboration();

    input.getMembers().forEach(m -> output.addMember(memberSwhToModel(m)));
    input.getTeams().forEach(t -> output.addTeam(teamSwhToModel(t)));

    return output;
  }

  private NewMember memberSwhToModel(CollaborationMembership collaborationMembership) {
    NewMember member = new NewMember();
    member.setName(collaborationMembership.getName());
    member.setRole(getMainRole(collaborationMembership.getRoles()));
    return member;
  }

  private String getMainRole(List<CollaborationMembership.RolesEnum> roles) {

    if (roles.contains(CollaborationMembership.RolesEnum.EDIT)) {
      return "Designer";
    } else if (roles.contains(CollaborationMembership.RolesEnum.COMMENT)) {
      return "Collaborator";
    } else {
      return "Viewer";
    }
  }

  private NewTeam teamSwhToModel(CollaborationTeamMembership collaborationTeam) {
    NewTeam team = new NewTeam();
    team.setName(collaborationTeam.getName());
    team.setDescription(collaborationTeam.getTitle());
    return team;
  }

  public OpenAPI parseOpenApi(String apiAsString) throws Exception {
    OpenAPIV3Parser openAPIV3Parser = new OpenAPIV3Parser();
    ParseOptions options = new ParseOptions();
    options.setResolveFully(true);
    SwaggerParseResult parseResults = openAPIV3Parser.readContents(apiAsString, null, options);
    if (!parseResults.getMessages().isEmpty()) {
      throw new Exception("Parse errors");
    }
    return parseResults.getOpenAPI();
  }

  public NewOpenApiDocument specificationAsStringToOpenApiDocument(String specificationAsString) throws Exception {
    NewOpenApiDocument openApiDocument = new NewOpenApiDocument();

//    openApiDocument.setDefinition(ClobProxy.generateProxy(specificationAsString));
    openApiDocument.setDefinition(specificationAsString);
    return openApiDocument;
  }

  private byte[] openApiToByteArray(OpenAPI openAPI) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(openAPI);
    oos.flush();
    return baos.toByteArray();
  }

  private String apiStringYaml(String specificationAsString) {
    return new Yaml().dump(specificationAsString);
  }


  public org.jfo.swaggerhub.swhreporter.model.db.Project projectSwhToModel(Project project) {
    org.jfo.swaggerhub.swhreporter.model.db.Project dbProject = new org.jfo.swaggerhub.swhreporter.model.db.Project();
    dbProject.setName(project.getName());
    dbProject.setDescription(project.getDescription());
    project.getApis().forEach(api -> dbProject.addApi(new Api(api)));
    project.getDomains().forEach(api -> dbProject.addDomain(new Domain(api)));
    return dbProject;
  }
}
