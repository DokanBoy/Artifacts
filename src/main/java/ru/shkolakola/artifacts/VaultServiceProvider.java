package ru.shkolakola.artifacts;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import ru.abstractcoder.benioapi.util.optional.BeniOptional;

/**
 * @author Alexey Zakharov
 * @date 23.05.2020
 */
public class VaultServiceProvider {

    private final Economy economy = (Economy) ((RegisteredServiceProvider)
            BeniOptional.ofNullable(Bukkit.getServicesManager().getRegistration(Economy.class))
                    .orThrow(() -> new NullPointerException("Economy plugin couldn't be found")))
            .getProvider();

    public void depositMoney(Player player, double amount) {
        economy.depositPlayer(player, amount).transactionSuccess();
    }

    public boolean withdrawMoney(Player player, double amount) {
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

}
