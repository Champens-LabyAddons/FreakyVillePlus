package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.event.housing.LivingAreaLookupEvent;
import dk.fvtrademarket.fvplus.api.housing.LivingArea;
import dk.fvtrademarket.fvplus.api.service.housing.HousingService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import java.util.Optional;

public class LivingAreaListener {
  private final ClientInfo clientInfo;
  private final ChatExecutor chatExecutor;
  private final HousingService housingService;

  public LivingAreaListener(ClientInfo clientInfo, ChatExecutor chatExecutor, HousingService housingService) {
    this.clientInfo = clientInfo;
    this.chatExecutor = chatExecutor;
    this.housingService = housingService;
  }

  @Subscribe
  public void onLivingAreaLookup(LivingAreaLookupEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    if (event.getAreaIdentifier().equals("Celle Kommandoer")) {
      this.chatExecutor.displayClientMessage(headerComponent(event.getAreaIdentifier()));
      return;
    }
    Optional<LivingArea> livingArea =
        this.housingService.getLivingArea(this.clientInfo.getCurrentServer(), event.getAreaIdentifier());
    if (livingArea.isEmpty()) {
      return;
    }
    TextComponent areaComponent = Component.text(livingArea.get().getDescription(), NamedTextColor.GRAY);
    this.chatExecutor.displayClientMessage(headerComponent(event.getAreaIdentifier()));
    this.chatExecutor.displayClientMessage(
        Component.translatable("fvplus.server.prison.cell.location", NamedTextColor.AQUA, areaComponent));
  }

  private TextComponent headerComponent(String header) {
    return Component.text("---==={", NamedTextColor.AQUA)
        .append(Component.text(" " + header + " ", NamedTextColor.GOLD))
        .append(Component.text("}===---", NamedTextColor.AQUA));
  }

}
