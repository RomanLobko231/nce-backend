package com.nce.backend.cars.infrastructure.externalApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

//'Vegvesen' is the name for the Norwegian road inspection agency.
@Service
public class VegvesenApiJsonMapper {

    public ApiCarData mapFromJson(JsonNode jsonNode) {
        return ApiCarData.builder()
                .make(
                        jsonNode
                                .get("kjoretoydataListe")
                                .get(0)
                                .get("godkjenning")
                                .get("tekniskGodkjenning")
                                .get("tekniskeData")
                                .get("generelt")
                                .get("merke")
                                .get(0)
                                .get("merke")
                                .asText()
                )
                .model(jsonNode.get("handelsbetegnelse").asText())
                .color(jsonNode.get("farge").asText())
                .weight(jsonNode.get("egenvekt").asInt())
                .numberOfSeats(jsonNode.get("sitteplasserTotalt").asInt())
                .numberOfDoors(jsonNode.get("antallDorer").get(0).asInt())
                .engineVolume(jsonNode.get("slagvolum").asInt())
                .engineType(jsonNode.get("drivstoffKode").get("kodeBeskrivelse").asText())
                .bodywork(jsonNode.path("karosseritype").path("kodeNavn").asText("N/A"))
                .firstTimeRegisteredInNorway(
                        LocalDate.parse(jsonNode.get("forstegangRegistrertDato").asText())
                )
                .nextEUControl(
                        LocalDate.parse(jsonNode.get("kontrollfrist").asText())
                )
                .gearboxType(
                        GearboxType.fromString(
                                jsonNode.get("girkassetype").get("kodeNavn").asText()
                        )

                )
                .operatingMode(defineOperatingMode(jsonNode))
                .build();
    }

    OperatingMode defineOperatingMode(JsonNode jsonNode) {
        JsonNode frontAxle = jsonNode.path("akselGruppe").path(0).path("aksel").path(0).path("drivAksel");
        JsonNode backAxle = jsonNode.path("akselGruppe").path(1).path("aksel").path(0).path("drivAksel");

        if (frontAxle.isMissingNode() || backAxle.isMissingNode()) return OperatingMode.OTHER;

        if (frontAxle.asBoolean() && !backAxle.asBoolean()) return OperatingMode.FRONT_WHEEL;
        if (!frontAxle.asBoolean() && backAxle.asBoolean()) return OperatingMode.REAR_WHEEL;

        return OperatingMode.FOUR_WHEEL;
    }


}
