package com.nce.backend.cars.infrastructure.externalApi;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

//'Vegvesen' is the name for the Norwegian road inspection agency.
@Service
public class VegvesenApiJsonMapper {

    private final String TEXT_DEFAULT_VALUE = "N/A";
    private final int INT_DEFAULT_VALUE = 0;


    public ApiCarData mapFromJson(JsonNode jsonNode) {
        JsonNode technicalDetailsNode = jsonNode
                .path("kjoretoydataListe")
                .path(0)
                .at("/godkjenning/tekniskGodkjenning/tekniskeData");


        return ApiCarData.builder()
                .make(
                        technicalDetailsNode
                                .at("/generelt/merke")
                                .path(0)
                                .path("merke")
                                .asText(TEXT_DEFAULT_VALUE)
                )
                .model(
                        technicalDetailsNode
                                .at("/generelt/handelsbetegnelse")
                                .path(0)
                                .asText(TEXT_DEFAULT_VALUE)
                )
                .color(
                        technicalDetailsNode
                                .at("/karosseriOgLasteplan/rFarge")
                                .path(0)
                                .path("kodeNavn")
                                .asText(TEXT_DEFAULT_VALUE)
                )
                .weight(
                        technicalDetailsNode
                                .at("/vekter/egenvekt")
                                .asInt(INT_DEFAULT_VALUE)
                )
                .numberOfSeats(
                        technicalDetailsNode
                                .at("/persontall/sitteplasserTotalt")
                                .asInt(INT_DEFAULT_VALUE)
                )
                .numberOfDoors(
                        technicalDetailsNode
                                .at("/karosseriOgLasteplan/antallDorer")
                                .path(0)
                                .asInt(INT_DEFAULT_VALUE)
                )
                .engineVolume(
                        technicalDetailsNode
                                .at("/motorOgDrivverk/motor")
                                .path(0)
                                .path("slagvolum")
                                .asInt(INT_DEFAULT_VALUE)
                )
                .engineType(
                        technicalDetailsNode
                                .at("/motorOgDrivverk/motor")
                                .path(0)
                                .path("drivstoff")
                                .path(0)
                                .at("/drivstoffKode/kodeBeskrivelse")
                                .asText(TEXT_DEFAULT_VALUE)
                )
                .bodywork(
                        technicalDetailsNode
                                .at("/karosseriOgLasteplan/karosseritype/kodeNavn")
                                .asText(TEXT_DEFAULT_VALUE)
                )
                .firstTimeRegisteredInNorway(
                        LocalDate.parse(
                                jsonNode
                                        .path("kjoretoydataListe")
                                        .path(0)
                                        .at("/forstegangsregistrering/registrertForstegangNorgeDato")
                                        .asText("1111-11-11")
                        )
                )
                .nextEUControl(
                        LocalDate.parse(
                                jsonNode
                                        .path("kjoretoydataListe")
                                        .path(0)
                                        .at("/periodiskKjoretoyKontroll/kontrollfrist")
                                        .asText("1111-11-11")
                        )
                )
                .gearboxType(
                        GearboxType.fromString(
                                technicalDetailsNode
                                        .at("/motorOgDrivverk/girkassetype/kodeNavn")
                                        .asText(TEXT_DEFAULT_VALUE)
                        )

                )
                .operatingMode(defineOperatingMode(technicalDetailsNode))
                .build();
    }

    private OperatingMode defineOperatingMode(JsonNode jsonNode) {
        JsonNode frontAxle = jsonNode.at("/akslinger/akselGruppe").path(0).at("/akselListe/aksel").path(0).path("drivAksel");
        JsonNode backAxle = jsonNode.at("/akslinger/akselGruppe").path(1).at("/akselListe/aksel").path(0).path("drivAksel");

        if (frontAxle.isMissingNode() || backAxle.isMissingNode()) return OperatingMode.OTHER;

        if (frontAxle.asBoolean() && !backAxle.asBoolean()) return OperatingMode.FRONT_WHEEL;
        if (!frontAxle.asBoolean() && backAxle.asBoolean()) return OperatingMode.REAR_WHEEL;

        return OperatingMode.FOUR_WHEEL;
    }


}
