package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}


	public Page<SaleReportDTO> getReport(String minDt, String maxDt, String name, Pageable pageable) {
		LocalDate minDate = (minDt.isEmpty()) ? LocalDate.now().minusYears(1L) : LocalDate.parse(minDt);
		LocalDate maxDate = (maxDt.isEmpty()) ? LocalDate.now() : LocalDate.parse(maxDt);

		Page<SaleReportDTO> page = (name.isEmpty()) ?
				repository.getReport(pageable, minDate, maxDate) :
				repository.getReport(pageable, minDate, maxDate, name.trim());
		return page.map(x  -> new SaleReportDTO(x.getId(), x.getDate(), x.getAmount(), x.getSellerName()));
	}

	public Page<SaleSummaryDTO> getSummary(String minDt, String maxDt, Pageable pageable) {
		LocalDate minDate = (minDt.isEmpty()) ? LocalDate.now().minusYears(1L) : LocalDate.parse(minDt);
		LocalDate maxDate = (maxDt.isEmpty()) ? LocalDate.now() : LocalDate.parse(maxDt);

		Page<SaleSummaryDTO> page = repository.getSummary(pageable, minDate, maxDate);
		return page.map(x  -> new SaleSummaryDTO(x.getSellerName(), x.getTotal()));
	}
}
