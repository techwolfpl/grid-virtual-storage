package io.techwolf.gvs.domain;

import io.techwolf.gvs.Settings;
import io.techwolf.gvs.domain.grid.port.EnergyGridPersistencePort;
import io.techwolf.gvs.domain.grid.port.EnergyGridProviderPort;
import java.math.BigDecimal;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RegisterGridReadingsService implements RegisterGridReadingsUseCase {

  private EnergyGridProviderPort energyGridProviderPort;
  private EnergyGridPersistencePort energyGridPersistencePort;
  private PostMessagePort postMessagePort;
  private Settings settings;


  public RegisterGridReadingsService(EnergyGridProviderPort energyGridProviderPort,
      EnergyGridPersistencePort energyGridPersistencePort, @Lazy PostMessagePort postMessagePort,
      final Settings settings) {
    this.energyGridProviderPort = energyGridProviderPort;
    this.energyGridPersistencePort = energyGridPersistencePort;
    this.postMessagePort = postMessagePort;
    this.settings = settings;
  }

  //  @Override
  //  @Transactional
  //  public void register(BigDecimal forwardEnergy, BigDecimal reverseEnergy) {
  //    var grid = energyGridProviderPort.getEnergyGrid();
  //    grid.updateReadings(forwardEnergy, reverseEnergy);
  //    energyGridPersistencePort.persist(grid);
  //  }

  @Override
  @Transactional
  public HashMap<String, String> registerForward(BigDecimal forwardEnergy) {
    var grid = energyGridProviderPort.getEnergyGrid();
    if (!grid.getReportingEnabled()) {
      return new HashMap<>();
    }
    grid.updateReadings(forwardEnergy, grid.getRealReverseEnergy(), settings, false);
    energyGridPersistencePort.persist(grid);
    return prepareReadingsToPublication(grid);
  }

  @Override
  @Transactional
  public HashMap<String, String> registerReverse(BigDecimal reverseEnergy) {
    var grid = energyGridProviderPort.getEnergyGrid();
    if (!grid.getReportingEnabled()) {
      return new HashMap<>();
    }
    grid.updateReadings(grid.getRealForwardEnergy(), reverseEnergy, settings, false);
    energyGridPersistencePort.persist(grid);
    return prepareReadingsToPublication(grid);
  }

  @Scheduled(cron = "#{@settings.periodicalFlushCron}")
  //@Scheduled(cron = "58 59 * * * *")
  @Transactional
  public void dumpPeriod() {
    long startTime = System.currentTimeMillis();
    log.info("Flushing periodical storage");

    if (!settings.getPeriodicalBalancing()) {
      return;
    }
    var grid = energyGridProviderPort.getEnergyGrid();
    if (!grid.getReportingEnabled()) {
      return;
    }
    grid.updateReadings(grid.getRealForwardEnergy(), grid.getRealReverseEnergy(), settings, true);
    energyGridPersistencePort.persist(grid);
    var updates = prepareReadingsToPublication(grid);
    updates.forEach((k, v) -> postMessagePort.post(k, v, 0));
    log.info("Periodical storage fluehed in {} ms", System.currentTimeMillis() - startTime);
  }

  private HashMap<String, String> prepareReadingsToPublication(EnergyGrid grid) {
    String domain = "techwolf-pl";
    var map = new HashMap<String, String>();
    map.put(domain + "/devices/grid-virtual-storage/grids/main/state/total_grid_forward_energy",
        grid.getForwardEnergy().toString());
    map.put(domain + "/devices/grid-virtual-storage/grids/main/state/total_grid_reverse_energy",
        grid.getReverseEnergy().toString());
    map.put(domain + "/devices/grid-virtual-storage/grids/main/state/total_storage_used_energy",
        grid.getEnergyUsedFromStorage().toString());
    map.put(domain + "/devices/grid-virtual-storage/grids/main/state/total_storage_charged_energy",
        grid.getEnergyChargedToStorage().toString());
    map.put(domain + "/devices/grid-virtual-storage/grids/main/state/storages/main/energy_stored",
        grid.getStorages().get(0).getEnergy().toString());

    if (settings.getPeriodicalBalancing()) {
      map.put(domain + "/devices/grid-virtual-storage/grids/main/state/current_periond_energy_balance",
          grid.getCurrentPeriodBalanceSum().toString());
    }

    return map;
  }
}
