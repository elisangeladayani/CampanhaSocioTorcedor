package com.backoffice;

import com.backoffice.http.CampanhaController;
import com.backoffice.entities.exception.CampanhaExpiradaException;
import com.backoffice.entities.exception.CampanhaNaoEncontradaException;
import com.backoffice.entities.Campanha;
import com.backoffice.gateway.repository.CampanhaRepository;
import com.backoffice.gateway.repository.TimeRepository;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CampanhaIntegrationTests {

	@Autowired
	private CampanhaController campanhaController;

	@Autowired
	private CampanhaRepository campanhaRepository;

	@Autowired
	private TimeRepository timeRepository;

	public static Date geraDataFuturo() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		String toParse = "01-01-2018 02:30";
		Date date = df.parse(toParse);
		return date;
	}

	public static Date geraData(String dataToParse) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = df.parse(dataToParse);
		return date;
	}

	public Date geraDataAtual(){
		return new Date();
	}

	@Test
	@Rollback
	public void getValidCampanhaByIdTest() throws ParseException, IllegalArgumentException {
		Campanha c = new Campanha("Campanha getValidCampanhaByIdTest", timeRepository.findOne(1000l), geraDataAtual(), geraDataFuturo());
		c = campanhaController.createCampanha(c);
		campanhaController.getValidCampanhaById(c.getId());
		assertEquals(c.getNome(), ((Campanha)campanhaController.getValidCampanhaById(c.getId())).getNome());
	}

	@Test(expected = CampanhaExpiradaException.class)
	@Rollback
	public void getValidCampanhaByIdFailDtVigenciaTest() throws ParseException, IllegalArgumentException {
		Campanha c = new Campanha("Campanha getValidCampanhaByIdTest", timeRepository.findOne(1000l), geraDataAtual(), geraDataAtual());
		c = campanhaController.createCampanha(c);
		campanhaController.getValidCampanhaById(c.getId());
		assertEquals(c.getNome(), ((Campanha)campanhaController.getValidCampanhaById(c.getId())).getNome());
	}

	@Test(expected = CampanhaNaoEncontradaException.class)
	@Rollback
	public void getValidCampanhaByIdFailNotFoundTest() throws ParseException {
		campanhaController.getValidCampanhaById(-1l);
	}

	@Test
	@Rollback
	public void createCampanhaTest() throws ParseException, IllegalArgumentException {
		Campanha c = new Campanha("Campanha System Test", timeRepository.findOne(1000l), geraDataAtual(), geraDataFuturo());
		c = campanhaController.createCampanha(c);
		assertEquals("Campanha System Test", ((Campanha)campanhaController.getValidCampanhaById(c.getId())).getNome());
	}


	@Test
	@Rollback
	public void createCampanhasVigenciasColidindoTest() throws ParseException, IllegalArgumentException {
		Campanha c = new Campanha("Campanha 1 System Test", timeRepository.findOne(1000l), geraDataAtual(), geraDataFuturo());
		c = campanhaController.createCampanha(c);

		Campanha c1 = new Campanha("Campanha 2 System Test", timeRepository.findOne(1000l), geraDataAtual(), geraDataFuturo());
		c1 = campanhaController.createCampanha(c1);

		DateTime dateTime = new DateTime(geraDataFuturo());
		assertEquals(dateTime.plusDays(1).toDate(), c.getDtFim());
	}

	@Test
	@Rollback
	public void createTresCampanhasVigenciasColidindoTest() throws ParseException, IllegalArgumentException {
		Campanha c1 = new Campanha("Campanha 2 Test", timeRepository.findOne(1000l), geraData("01-10-2017 02:30"), geraData("02-10-2017 02:30"));
		c1 = campanhaController.createCampanha(c1);

		Campanha c2 = new Campanha("Campanha 1 Test", timeRepository.findOne(1000l), geraData("01-10-2017 02:30"), geraData("03-10-2017 02:30"));
		c2 = campanhaController.createCampanha(c2);

		Campanha c3 = new Campanha("Campanha 3 Test", timeRepository.findOne(1000l), geraData("01-10-2017 02:30"), geraData("03-10-2017 02:30"));
		c3 = campanhaController.createCampanha(c3);

		assertEquals(geraData("05-10-2017 02:30"), c1.getDtFim());
		assertEquals(geraData("04-10-2017 02:30"), c2.getDtFim());
		assertEquals(geraData("03-10-2017 02:30"), c3.getDtFim());
	}


	@Test(expected = IllegalArgumentException.class)
	@Rollback
	public void createCampanhaFailArgumentTest() throws ParseException, IllegalArgumentException {
		Campanha c = new Campanha();
		c.setDtFim(geraDataAtual());
		c.setDtInicio(geraDataFuturo());
		campanhaController.createCampanha(c);
	}

	@Test
	@Rollback
	public void updateCampanhaTest() throws ParseException, IllegalArgumentException {
		Campanha c = new Campanha("Campanha updateCampanhaTest System Test", timeRepository.findOne(1000l), geraDataAtual(), geraDataFuturo());
		c = campanhaController.createCampanha(c);
		Campanha campanhaUpdate = (Campanha) campanhaController.getValidCampanhaById(c.getId());
		campanhaUpdate.setNome("Campanha atualizada.");
		campanhaController.updateCampanha(campanhaUpdate, campanhaUpdate.getId());
		assertEquals("Campanha atualizada.", ((Campanha)campanhaController.getValidCampanhaById(c.getId())).getNome());
	}


	@Test(expected = CampanhaNaoEncontradaException.class)
	@Rollback
	public void updateCampanhaFailNotFoundTest() throws ParseException {
		campanhaController.updateCampanha(new Campanha(), -1l);
	}

	@Test(expected = CampanhaNaoEncontradaException.class)
	@Rollback
	public void deleteCampanhaTest() throws ParseException, IllegalArgumentException {
		Campanha c = new Campanha("Campanha updateCampanhaTest System Test", timeRepository.findOne(1000l), geraDataAtual(), geraDataFuturo());
		c = campanhaController.createCampanha(c);
		c = campanhaController.deleteCampanha(c.getId());
		campanhaController.getValidCampanhaById(c.getId());
	}

	@Test(expected = CampanhaNaoEncontradaException.class)
	@Rollback
	public void deleteCampanhaFailNotFoundTest() throws ParseException {
		campanhaController.deleteCampanha(-1l);
	}

}
