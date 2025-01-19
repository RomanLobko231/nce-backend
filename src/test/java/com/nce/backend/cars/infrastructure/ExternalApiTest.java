package com.nce.backend.cars.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import com.nce.backend.cars.infrastructure.externalApi.VegvesenApiJsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ExternalApiTest {

    @Autowired
    private VegvesenApiJsonMapper mapper;


    @Test
    public void testJsonMapper() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"kjoretoydataListe\":[{\"kjoretoyId\":{\"kjennemerke\":\"DL 14098\",\"understellsnummer\":\"JTFNS21YX00010350\"},\"forstegangsregistrering\":{\"registrertForstegangNorgeDato\":\"2005-06-02\"},\"kjennemerke\":[{\"fomTidspunkt\":\"2005-06-02T00:00:00+02:00\",\"kjennemerke\":\"DL 14098\",\"kjennemerkekategori\":\"KJORETOY\",\"kjennemerketype\":{\"kodeBeskrivelse\":\"Sorte tegn på grønn bunn\",\"kodeNavn\":\"Varebil klasse 2\",\"kodeVerdi\":\"VAREBIL_KLASSE_2\",\"tidligereKodeVerdi\":[]}}],\"registrering\":{\"fomTidspunkt\":\"2025-01-17T15:40:02.178+01:00\",\"kjoringensArt\":{\"kodeBeskrivelse\":\"Annen egentransport.\",\"kodeNavn\":\"Egentransport\",\"kodeVerdi\":\"EGENTRANSP_ANNEN\",\"tidligereKodeVerdi\":[]},\"registreringsstatus\":{\"kodeBeskrivelse\":\"Avregistrert\",\"kodeNavn\":\"Midlertidig avregistrert\",\"kodeVerdi\":\"AVREGISTRERT\",\"tidligereKodeVerdi\":[]},\"avregistrertSidenDato\":\"2025-01-17T15:40:02.178+01:00\"},\"godkjenning\":{\"forstegangsGodkjenning\":{\"forstegangRegistrertDato\":\"2005-06-02\",\"godkjenningsId\":\"1001066830983\",\"godkjenningsundertype\":{\"kodeNavn\":\"COC\",\"kodeVerdi\":\"COC\",\"tidligereKodeVerdi\":[]},\"gyldigFraDato\":\"2005-05-24\",\"gyldigFraDatoTid\":\"2005-05-24T00:00:00+02:00\",\"unntak\":[]},\"kjoretoymerknad\":[],\"registreringsbegrensninger\":{\"registreringsbegrensning\":[]},\"tekniskGodkjenning\":{\"godkjenningsId\":\"1001066830983\",\"godkjenningsundertype\":{\"kodeNavn\":\"COC\",\"kodeVerdi\":\"COC\",\"tidligereKodeVerdi\":[]},\"gyldigFraDato\":\"2005-05-24\",\"gyldigFraDatoTid\":\"2005-05-24T00:00:00+02:00\",\"kjoretoyklassifisering\":{\"beskrivelse\":\"Varebil kl 2\",\"efTypegodkjenning\":{\"typegodkjenningNrTekst\":\"e16*NOR2001*0214*00\",\"typegodkjenningnummer\":{\"direktiv\":\"NOR2001\",\"land\":\"e16\",\"serie\":\"0214\",\"utvidelse\":\"00\"},\"variant\":\"1\"},\"kjoretoyAvgiftsKode\":{\"kodeBeskrivelse\":\"Varebil (Klasse 2),- fortollet etter 31.3.2001\",\"kodeNavn\":\"Varebil Klasse 2 - etter 31.3.2001\",\"kodeVerdi\":\"315\",\"tidligereKodeVerdi\":[]},\"nasjonalGodkjenning\":{\"nasjonaltGodkjenningsAr\":\"2001\",\"nasjonaltGodkjenningsHovednummer\":\"0214\",\"nasjonaltGodkjenningsUndernummer\":\"001\"},\"spesielleKjennetegn\":\"Teknisk varebil, avgiftsmessig varebil kl 2\",\"tekniskKode\":{\"kodeBeskrivelse\":\"Bil for godsbefordring med tillatt totalvekt ikke over 3 500 kg (Varebil)\",\"kodeNavn\":\"Varebil\",\"kodeVerdi\":\"N1\",\"tidligereKodeVerdi\":[]},\"tekniskUnderkode\":{\"kodeVerdi\":\"INGENKODE\",\"tidligereKodeVerdi\":[]},\"iSamsvarMedTypegodkjenning\":true},\"krav\":[{\"kravomrade\":{\"kodeBeskrivelse\":\"Avgiftsklassifisering\",\"kodeVerdi\":\"AVGIFTSKLASSIFISERING_00\",\"tidligereKodeVerdi\":[]},\"kravoppfyllelse\":{\"kodeBeskrivelse\":\"Forskrift om engangsavgift og avgiftsvedtak\",\"kodeVerdi\":\"KRAV_1\",\"tidligereKodeVerdi\":[]}}],\"tekniskeData\":{\"akslinger\":{\"akselGruppe\":[{\"akselListe\":{\"aksel\":[{\"avstandTilNesteAksling\":2300,\"drivAksel\":false,\"egenvektAksel\":1110,\"id\":1,\"plasseringAksel\":\"1\",\"sporvidde\":1435,\"tekniskTillattAkselLast\":1800}]},\"egenvektAkselGruppe\":1110,\"id\":1,\"plasseringAkselGruppe\":\"1\",\"tekniskTillattAkselGruppeLast\":1800},{\"akselListe\":{\"aksel\":[{\"drivAksel\":true,\"egenvektAksel\":595,\"id\":2,\"plasseringAksel\":\"2\",\"sporvidde\":1365,\"tekniskTillattAkselLast\":1800}]},\"egenvektAkselGruppe\":595,\"id\":2,\"plasseringAkselGruppe\":\"2\",\"tekniskTillattAkselGruppeLast\":1800}],\"antallAksler\":2},\"bremser\":{\"tilhengerBremseforbindelse\":[]},\"dekkOgFelg\":{\"akselDekkOgFelgKombinasjon\":[{\"akselDekkOgFelg\":[{\"akselId\":1,\"belastningskodeDekk\":\"104\",\"dekkdimensjon\":\"195/70R15C\",\"felgdimensjon\":\"5J\",\"hastighetskodeDekk\":\"M\",\"innpress\":\"29\"},{\"akselId\":2,\"belastningskodeDekk\":\"104\",\"dekkdimensjon\":\"195/70R15C\",\"felgdimensjon\":\"5J\",\"hastighetskodeDekk\":\"M\",\"innpress\":\"29\"}]},{\"akselDekkOgFelg\":[{\"akselId\":1,\"belastningskodeDekk\":\"104\",\"felgdimensjon\":\"5J\",\"hastighetskodeDekk\":\"M\",\"innpress\":\"29\"},{\"akselId\":2,\"belastningskodeDekk\":\"104\",\"felgdimensjon\":\"5J\",\"hastighetskodeDekk\":\"M\",\"innpress\":\"29\"}]}]},\"dimensjoner\":{\"bredde\":1700,\"lengde\":4430},\"generelt\":{\"fabrikant\":[],\"handelsbetegnelse\":[\"DYNA\"],\"merke\":[{\"merke\":\"TOYOTA\",\"merkeKode\":\"5480\"}],\"tekniskKode\":{\"kodeBeskrivelse\":\"Bil for godsbefordring med tillatt totalvekt ikke over 3 500 kg (Varebil)\",\"kodeNavn\":\"Varebil\",\"kodeVerdi\":\"N1\",\"tidligereKodeVerdi\":[]},\"typebetegnelse\":\"KDY220L-TBMDYW\"},\"karosseriOgLasteplan\":{\"antallDorer\":[],\"dorUtforming\":[],\"kjennemerketypeBak\":{\"kodeBeskrivelse\":\"Stort smalt\",\"kodeNavn\":\"Stort smalt\",\"kodeTypeId\":\"TEKNISKEDATA.KJENNEMERKESTORRELSE\",\"kodeVerdi\":\"SS\",\"tidligereKodeVerdi\":[]},\"kjennemerkestorrelseBak\":{\"kodeBeskrivelse\":\"Stort smalt\",\"kodeNavn\":\"Stort smalt\",\"kodeTypeId\":\"TEKNISKEDATA.KJENNEMERKESTORRELSE\",\"kodeVerdi\":\"SS\",\"tidligereKodeVerdi\":[]},\"kjennemerketypeForan\":{\"kodeBeskrivelse\":\"Stort smalt\",\"kodeNavn\":\"Stort smalt\",\"kodeTypeId\":\"TEKNISKEDATA.KJENNEMERKESTORRELSE\",\"kodeVerdi\":\"SS\",\"tidligereKodeVerdi\":[]},\"kjennemerkestorrelseForan\":{\"kodeBeskrivelse\":\"Stort smalt\",\"kodeNavn\":\"Stort smalt\",\"kodeTypeId\":\"TEKNISKEDATA.KJENNEMERKESTORRELSE\",\"kodeVerdi\":\"SS\",\"tidligereKodeVerdi\":[]},\"kjoringSide\":\"HOYRE\",\"oppbygningUnderstellsnummer\":\"JTFNS21Y?00001001\",\"plasseringFabrikasjonsplate\":[{\"kodeNavn\":\"Ingen opplysninger\",\"kodeTypeId\":\"TEKNISKEDATA.PLASSERINGMERKING\",\"kodeVerdi\":\"INGEN\",\"tidligereKodeVerdi\":[]}],\"plasseringUnderstellsnummer\":[{\"kodeNavn\":\"Ingen opplysninger\",\"kodeTypeId\":\"TEKNISKEDATA.PLASSERINGMERKING\",\"kodeVerdi\":\"INGEN\",\"tidligereKodeVerdi\":[]}],\"rFarge\":[{\"kodeBeskrivelse\":\"herunder: Sølv metallic\",\"kodeNavn\":\"Sølv\",\"kodeTypeId\":\"TEKNISKEDATA.KAROSSERIFARGE\",\"kodeVerdi\":\"11\",\"tidligereKodeVerdi\":[]}]},\"miljodata\":{\"euroKlasse\":{\"kodeBeskrivelse\":\"Euro 4L - 70/220/EØF*2003/76/EF - omfatter også tunge bensin M og N\",\"kodeNavn\":\"Euro 4L - 70/220/EØF*2003/76/EF\",\"kodeTypeId\":\"TEKNISKEDATA.AVGASSKODE\",\"kodeVerdi\":\"4L\",\"tidligereKodeVerdi\":[]},\"miljoOgdrivstoffGruppe\":[{\"drivstoffKodeMiljodata\":{\"kodeBeskrivelse\":\"Diesel\",\"kodeNavn\":\"Diesel\",\"kodeTypeId\":\"TEKNISKEDATA.DRIVSTOFFTYPE\",\"kodeVerdi\":\"2\",\"tidligereKodeVerdi\":[]},\"lyd\":{\"standstoy\":84,\"stoyMalingOppgittAv\":{\"kodeBeskrivelse\":\"Produsent\",\"kodeNavn\":\"Produsent\",\"kodeTypeId\":\"TEKNISKEDATA.KILDE_STOYMALING\",\"kodeVerdi\":\"1\",\"tidligereKodeVerdi\":[]},\"vedAntallOmdreininger\":2850}}],\"okoInnovasjon\":false},\"motorOgDrivverk\":{\"girkassetype\":{\"kodeBeskrivelse\":\"Manuell\",\"kodeNavn\":\"Manuell\",\"kodeTypeId\":\"TEKNISKEDATA.GIRKASSETYPE\",\"kodeVerdi\":\"M\",\"tidligereKodeVerdi\":[]},\"girutvekslingPrGir\":[],\"hybridKategori\":{\"kodeBeskrivelse\":\"Ingen\",\"kodeNavn\":\"Ingen\",\"kodeTypeId\":\"TEKNISKEDATA.HYBRIDKATEGORI\",\"kodeVerdi\":\"INGEN\",\"tidligereKodeVerdi\":[]},\"maksimumHastighet\":[130],\"maksimumHastighetMalt\":[],\"motor\":[{\"antallSylindre\":4,\"drivstoff\":[{\"drivstoffKode\":{\"kodeBeskrivelse\":\"Diesel\",\"kodeNavn\":\"Diesel\",\"kodeTypeId\":\"TEKNISKEDATA.DRIVSTOFFTYPE\",\"kodeVerdi\":\"2\",\"tidligereKodeVerdi\":[]},\"maksNettoEffekt\":65.0}],\"motorKode\":\"2KD-FTV\",\"slagvolum\":2494}]},\"ovrigeTekniskeData\":[],\"persontall\":{\"sitteplasserForan\":3,\"sitteplasserTotalt\":3},\"tilhengerkopling\":{\"kopling\":[]},\"vekter\":{\"egenvekt\":1705,\"egenvektMinimum\":1705,\"nyttelast\":1220,\"tillattTaklast\":30,\"tillattTilhengervektMedBrems\":2000,\"tillattTilhengervektUtenBrems\":750,\"tillattTotalvekt\":3000,\"tillattVertikalKoplingslast\":110,\"tillattVogntogvekt\":5000,\"vogntogvektAvhBremsesystem\":[]}},\"unntak\":[]},\"tilleggsgodkjenninger\":[]},\"periodiskKjoretoyKontroll\":{\"kontrollfrist\":\"2024-07-22\",\"sistGodkjent\":\"2022-08-05\"}}]}";
        JsonNode node = objectMapper.readTree(json);


        System.out.println(
                node
                        .get("kjoretoydataListe")
                        .get(0)
                        .get("godkjenning")
                        .get("tekniskGodkjenning")
                        .get("tekniskeData")
                        .get("generelt")
                        .get("merke")
                        .get(0)
                        .get("merke")
                        .asText());

//        ApiCarData apiData = mapper.mapFromJson(node);
//        assertEquals(apiData.bodywork(), "N/A");
//        assertEquals(apiData.color(), "Sølv");
//        assertEquals(apiData.engineVolume(), 2494);
//        assertEquals(apiData.firstTimeRegisteredInNorway(), LocalDate.parse("2005-06-02"));

    }
}
