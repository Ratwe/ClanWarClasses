package me.ergo.clanwarclasses.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Skills implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID id = player.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

            String Class;
            if(args.length == 0)
                Class = user.getGroupNames()[0];
            else
                Class = args[0];

            Inventory gui = Bukkit.createInventory(player, 9, ChatColor.BLACK + "Способности");
            ItemStack[] menu_items = new ItemStack[0];

            switch (Class) {
                case ("archer"):
                    gui = Bukkit.createInventory(player, 18, ChatColor.BLACK + "Способности");

                    /** Стремительность **/
                    ItemStack arrowSpeed = new ItemStack(Material.BOW);
                    ItemMeta arrowSpeed_meta = arrowSpeed.getItemMeta();

                    arrowSpeed_meta.setDisplayName(ChatColor.DARK_RED + "Стремительность" + ChatColor.GRAY + " (0)");
                    ArrayList<String> arrowSpeed_lore = new ArrayList<>();
                    arrowSpeed_lore.add(ChatColor.ITALIC + "Пассивно:" + ChatColor.WHITE + " выпущенные вами стрелы");
                    arrowSpeed_lore.add(ChatColor.WHITE + "летят в 2 раза быстрее");
                    arrowSpeed_meta.setLore(arrowSpeed_lore);
                    arrowSpeed.setItemMeta(arrowSpeed_meta);


                    /** Отскок **/
                    ItemStack rebound = new ItemStack(Material.IRON_BOOTS);
                    ItemMeta rebound_meta = rebound.getItemMeta();

                    rebound_meta.setDisplayName(ChatColor.YELLOW + "Отскок" + ChatColor.GRAY + " (10)");
                    ArrayList<String> rebound_lore = new ArrayList<>();
                    rebound_lore.add(ChatColor.WHITE + "При нажатии пробела трижды");
                    rebound_lore.add(ChatColor.WHITE + "вы отбрасываетесь назад");
                    rebound_lore.add("");
                    rebound_lore.add(ChatColor.GREEN + "Перезарядка: 5 секунд");
                    rebound_meta.setLore(rebound_lore);

                    rebound_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    rebound.setItemMeta(rebound_meta);


                    /** Стрела замедления **/
                    ItemStack arrowSlow = new ItemStack(Material.TIPPED_ARROW);

                    PotionMeta arrowPMetaS = (PotionMeta) arrowSlow.getItemMeta();
                    arrowPMetaS.setBasePotionData(new PotionData(PotionType.SLOWNESS));
                    arrowSlow.setItemMeta(arrowPMetaS);

                    ItemMeta arrowSlow_meta = arrowSlow.getItemMeta();
                    arrowSlow_meta.setDisplayName(ChatColor.DARK_GREEN + "Стрела медлительности" + ChatColor.GRAY + " (20)");
                    ArrayList<String> arrowSlow_lore = new ArrayList<>();
                    arrowSlow_lore.add(ChatColor.WHITE + "Выпускает стрелу медлительности");
                    arrowSlow_lore.add(ChatColor.WHITE + "по направлению взгляда");
                    arrowSlow_lore.add("");
                    arrowSlow_lore.add(ChatColor.RED + "Медлительность III (0:05)");
                    arrowSlow_lore.add(ChatColor.GREEN + "Перезарядка: 10 секунд");
                    arrowSlow_meta.setLore(arrowSlow_lore);

                    arrowSlow_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    arrowSlow.setItemMeta(arrowSlow_meta);


                    /** Стрела прыгучести **/
                    ItemStack arrowJump = new ItemStack(Material.TIPPED_ARROW);

                    PotionMeta arrowPMetaJ = (PotionMeta) arrowJump.getItemMeta();
                    arrowPMetaJ.setBasePotionData(new PotionData(PotionType.JUMP));
                    arrowJump.setItemMeta(arrowPMetaJ);

                    ItemMeta arrowJump_meta = arrowJump.getItemMeta();
                    arrowJump_meta.setDisplayName(ChatColor.DARK_GREEN + "Стрела прыгучести" + ChatColor.GRAY + " (20)");
                    ArrayList<String> arrowJump_lore = new ArrayList<>();
                    arrowJump_lore.add(ChatColor.WHITE + "Выпускает стрелу прыгучести");
                    arrowJump_lore.add(ChatColor.WHITE + "по направлению взгляда");
                    arrowJump_lore.add("");
                    arrowJump_lore.add(ChatColor.BLUE + "Прыгучесть III (0:10)");
                    arrowJump_lore.add(ChatColor.GREEN + "Перезарядка: 10 секунд");
                    arrowJump_meta.setLore(arrowJump_lore);

                    arrowJump_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    arrowJump.setItemMeta(arrowJump_meta);


                    /** Скорость **/
                    ItemStack speedPotion = getPotionItemStack(PotionType.SPEED, 0, true, false, null);
                    ItemMeta speedPotion_meta = speedPotion.getItemMeta();

                    speedPotion_meta.setDisplayName(ChatColor.DARK_AQUA + "Скорость" + ChatColor.GRAY + " (30)");
                    ArrayList<String> speedPotion_lore = new ArrayList<>();
                    speedPotion_lore.add(ChatColor.ITALIC + "Пассивно:");
                    speedPotion_lore.add(ChatColor.BLUE + "Скорость (**:**)");
                    speedPotion_meta.setLore(speedPotion_lore);

                    speedPotion_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    speedPotion.setItemMeta(speedPotion_meta);


                    /** Стрела отравления **/
                    ItemStack arrowPoison = new ItemStack(Material.TIPPED_ARROW);
                    PotionMeta arrowPoisonMetaP = (PotionMeta) arrowPoison.getItemMeta();
                    arrowPoisonMetaP.setBasePotionData(new PotionData(PotionType.POISON));
                    arrowPoison.setItemMeta(arrowPoisonMetaP);

                    ItemMeta arrowPoison_meta = arrowPoison.getItemMeta();
                    arrowPoison_meta.setDisplayName(ChatColor.AQUA + "Стрела отравления" + ChatColor.GRAY + " (40)");
                    ArrayList<String> arrowPoison_lore = new ArrayList<>();
                    arrowPoison_lore.add(ChatColor.WHITE + "Выпускает стрелу отравления");
                    arrowPoison_lore.add(ChatColor.WHITE + "по направлению взгляда");
                    arrowPoison_lore.add("");
                    arrowPoison_lore.add(ChatColor.RED + "Отравление III (0:03)");
                    arrowPoison_lore.add(ChatColor.GREEN + "Перезарядка: 10 секунд");
                    arrowPoison_meta.setLore(arrowPoison_lore);

                    arrowPoison_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    arrowPoison.setItemMeta(arrowPoison_meta);

                    /** Призрачная стрела **/
                    ItemStack arrowAstral = new ItemStack(Material.SPECTRAL_ARROW);

                    ItemMeta arrowAstral_meta = arrowAstral.getItemMeta();
                    arrowAstral_meta.setDisplayName(ChatColor.AQUA + "Призрачная стрела" + ChatColor.GRAY + " (40)");
                    ArrayList<String> arrowAstral_lore = new ArrayList<>();
                    arrowAstral_lore.add(ChatColor.WHITE + "Выпускает призрачную стрелу");
                    arrowAstral_lore.add(ChatColor.WHITE + "по направлению взгляда");
                    arrowAstral_lore.add("");
                    arrowAstral_lore.add(ChatColor.RED + "Свечение (0:10)");
                    arrowAstral_lore.add(ChatColor.GREEN + "Перезарядка: 10 секунд");
                    arrowAstral_meta.setLore(arrowAstral_lore);

                    arrowAstral.setItemMeta(arrowAstral_meta);

                    /** Взлёт **/
                    ItemStack ascent = new ItemStack(Material.FEATHER);
                    ItemMeta ascent_meta = ascent.getItemMeta();

                    ascent_meta.setDisplayName(ChatColor.GOLD + "Взлёт" + ChatColor.GRAY + " (50)");
                    ArrayList<String> ascent_lore = new ArrayList<>();
                    ascent_lore.add(ChatColor.WHITE + "Поднимает в воздух на 30 блоков");
                    ascent_lore.add(ChatColor.WHITE + "и замедляет падение");
                    ascent_lore.add("");
                    ascent_lore.add(ChatColor.BLUE + "Плавное падение (00:06)");
                    ascent_lore.add(ChatColor.GREEN + "Перезарядка: 20 секунд");
                    ascent_meta.setLore(ascent_lore);
                    ascent.setItemMeta(ascent_meta);

                    menu_items = new ItemStack[]{arrowSpeed, rebound, arrowSlow, speedPotion, arrowPoison, ascent, null, null, null, null, null, arrowJump, null, arrowAstral};
                    break;



                case ("assassin"):
                    /** Скорость **/
                    ItemStack speedAssassin = getPotionItemStack(PotionType.SPEED, 0, true, false, null);
                    ItemMeta speedAssassin_meta = speedAssassin.getItemMeta();

                    speedAssassin_meta.setDisplayName(ChatColor.DARK_RED + "Скорость" + ChatColor.GRAY + " (0)");
                    ArrayList<String> speedAssassin_lore = new ArrayList<>();
                    speedAssassin_lore.add(ChatColor.ITALIC + "Пассивно:");
                    speedAssassin_lore.add(ChatColor.BLUE + "Скорость (**:**)");
                    speedAssassin_meta.setLore(speedAssassin_lore);

                    speedAssassin_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    speedAssassin.setItemMeta(speedAssassin_meta);


                    /** Одиночка **/
                    ItemStack loner = new ItemStack(Material.PLAYER_HEAD);
                    ItemMeta loner_meta = loner.getItemMeta();

                    loner_meta.setDisplayName(ChatColor.YELLOW + "Одиночка" + ChatColor.GRAY + " (10)");
                    ArrayList<String> loner_lore = new ArrayList<>();
                    loner_lore.add(ChatColor.ITALIC + "Пассивно:");
                    loner_lore.add(ChatColor.WHITE + "Увеличение урона на 20% если");
                    loner_lore.add(ChatColor.WHITE + "в радиусе 16 блоков нет игроков,");
                    loner_lore.add(ChatColor.WHITE + "кроме вас, соклановцев и жертвы");
                    loner_meta.setLore(loner_lore);

                    loner_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    loner.setItemMeta(loner_meta);


                    /** Травма **/
                    ItemStack trauma = new ItemStack(Material.BONE);
                    ItemMeta trauma_meta = trauma.getItemMeta();

                    trauma_meta.setDisplayName(ChatColor.DARK_GREEN + "Травма" + ChatColor.GRAY + " (20)");
                    ArrayList<String> trauma_lore = new ArrayList<>();
                    trauma_lore.add(ChatColor.ITALIC + "Пассивно:");
                    trauma_lore.add(ChatColor.WHITE + "При ударе шанс 5%");
                    trauma_lore.add(ChatColor.WHITE + "наложить на игрока эффект");
                    trauma_lore.add("");
                    trauma_lore.add(ChatColor.RED + "Медлительность III (0:04)");
                    trauma_meta.setLore(trauma_lore);

                    trauma_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    trauma.setItemMeta(trauma_meta);


                    /** Засада **/
                    ItemStack burrow = new ItemStack(Material.WOODEN_SHOVEL);
                    ItemMeta burrow_meta = burrow.getItemMeta();

                    burrow_meta.setDisplayName(ChatColor.DARK_AQUA + "Засада" + ChatColor.GRAY + " (30)");
                    ArrayList<String> burrow_lore = new ArrayList<>();
                    burrow_lore.add(ChatColor.WHITE + "Нажмите ПКМ по блоку");
                    burrow_lore.add(ChatColor.WHITE + "на котором стоите, чтобы");
                    burrow_lore.add(ChatColor.WHITE + "'провалиться' в него");
                    burrow_lore.add(ChatColor.WHITE + "и получить эффект");
                    burrow_lore.add("");
                    burrow_lore.add(ChatColor.BLUE + "Невидимость (**:**)");
                    burrow_meta.setLore(burrow_lore);

                    burrow_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    burrow_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    burrow.setItemMeta(burrow_meta);


                    /** Ночное зрение **/
                    ItemStack nightVision = getPotionItemStack(PotionType.NIGHT_VISION, 0, true, false, null);
                    ItemMeta nightVision_meta = nightVision.getItemMeta();

                    nightVision_meta.setDisplayName(ChatColor.AQUA + "Ночное зрение" + ChatColor.GRAY + " (40)");
                    ArrayList<String> nightVision_lore = new ArrayList<>();
                    nightVision_lore.add(ChatColor.ITALIC + "Пассивно:");
                    nightVision_lore.add(ChatColor.BLUE + "Ночное зрение (**:**)");
                    nightVision_meta.setLore(nightVision_lore);

                    nightVision_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    nightVision.setItemMeta(nightVision_meta);


                    /** Стрела слепоты **/
                    ItemStack arrowBlind = new ItemStack(Material.TIPPED_ARROW);
                    PotionMeta arrowBlindMetaP = (PotionMeta) arrowBlind.getItemMeta();
                    arrowBlindMetaP.setBasePotionData(new PotionData(PotionType.WEAKNESS));
                    arrowBlind.setItemMeta(arrowBlindMetaP);

                    ItemMeta arrowBlind_meta = arrowBlind.getItemMeta();
                    arrowBlind_meta.setDisplayName(ChatColor.GOLD + "Стрела слепоты" + ChatColor.GRAY + " (50)");
                    ArrayList<String> arrowBlind_lore = new ArrayList<>();
                    arrowBlind_lore.add(ChatColor.WHITE + "При ударе соответствующей стрелой");
                    arrowBlind_lore.add(ChatColor.WHITE + "на игрока накладывается эффект");
                    arrowBlind_lore.add("");
                    arrowBlind_lore.add(ChatColor.RED + "Слепота II (0:05)");
                    arrowBlind_lore.add(ChatColor.GREEN + "Перезарядка: 20 секунд");
                    arrowBlind_meta.setLore(arrowBlind_lore);

                    arrowBlind_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    arrowBlind.setItemMeta(arrowBlind_meta);


                    menu_items =  new ItemStack[]{speedAssassin, loner, trauma, burrow, nightVision, arrowBlind};
                    break;



                case("healer"):
                    gui = Bukkit.createInventory(player, 18, ChatColor.BLACK + "Способности");

                    /** Небесная кара **/
                    ItemStack smite = new ItemStack(Material.ZOMBIE_HEAD);
                    ItemMeta smite_meta = smite.getItemMeta();

                    smite_meta.setDisplayName(ChatColor.DARK_RED + "Небесная кара" + ChatColor.GRAY + " (0)");
                    ArrayList<String> smite_lore = new ArrayList<>();
                    smite_lore.add(ChatColor.ITALIC + "Пассивно:");
                    smite_lore.add(ChatColor.WHITE + "Увеличение урона на 20%");
                    smite_lore.add(ChatColor.WHITE + "по нежити");
                    smite_meta.setLore(smite_lore);

                    smite_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    smite.setItemMeta(smite_meta);


                    /** Массовое лечение **/
                    ItemStack massiveHeal = getPotionItemStack(PotionType.INSTANT_HEAL, 0, false, true, null);
                    ItemMeta massiveHeal_meta = massiveHeal.getItemMeta();

                    massiveHeal_meta.setDisplayName(ChatColor.YELLOW + "Массовое лечение" + ChatColor.GRAY + " (10)");
                    ArrayList<String> massiveHeal_lore = new ArrayList<>();
                    massiveHeal_lore.add(ChatColor.WHITE + "При использовании зелья лечения,");
                    massiveHeal_lore.add(ChatColor.WHITE + "в радиусе 10 блоков");
                    massiveHeal_lore.add(ChatColor.WHITE + "соклановцы восстанавливают 4 HP");
                    massiveHeal_lore.add("");
                    massiveHeal_lore.add(ChatColor.BLUE + "Мгновенное лечение");
                    massiveHeal_lore.add(ChatColor.GREEN + "Перезарядка: 12 секунд");
                    massiveHeal_meta.setLore(massiveHeal_lore);

                    massiveHeal_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    massiveHeal.setItemMeta(massiveHeal_meta);


                    /** Массовая регенерация **/
                    ItemStack massiveRegen = getPotionItemStack(PotionType.REGEN, 0, true, false, null);
                    ItemMeta massiveRegen_meta = massiveRegen.getItemMeta();

                    massiveRegen_meta.setDisplayName(ChatColor.YELLOW + "Массовая регенерация" + ChatColor.GRAY + " (10)");
                    ArrayList<String> massiveRegen_lore = new ArrayList<>();
                    massiveRegen_lore.add(ChatColor.WHITE + "При использовании зелья регенерации");
                    massiveRegen_lore.add(ChatColor.WHITE + "соклановцы в радиусе 10 блоков");
                    massiveRegen_lore.add(ChatColor.WHITE + "получают эффект");
                    massiveRegen_lore.add("");
                    massiveRegen_lore.add(ChatColor.BLUE + "Регенерация (00:10)");
                    massiveRegen_lore.add(ChatColor.GREEN + "Перезарядка: 15 секунд");
                    massiveRegen_meta.setLore(massiveRegen_lore);

                    massiveRegen_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    massiveRegen.setItemMeta(massiveRegen_meta);


                    /** Увеличение восстановления **/
                    ItemStack regainHealth = new ItemStack(Material.BREAD);
                    ItemMeta regainHealth_meta = regainHealth.getItemMeta();

                    regainHealth_meta.setDisplayName(ChatColor.DARK_GREEN + "Увеличение восстановления" + ChatColor.GRAY + " (20)");
                    ArrayList<String> regainHealth_lore = new ArrayList<>();
                    regainHealth_lore.add(ChatColor.ITALIC + "Пассивно:");
                    regainHealth_lore.add(ChatColor.WHITE + "Любое восстановление здоровья");
                    regainHealth_lore.add(ChatColor.WHITE + "увеличено на 20%");
                    regainHealth_meta.setLore(regainHealth_lore);

                    regainHealth_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    regainHealth.setItemMeta(regainHealth_meta);


                    /** Отталкивание **/
                    ItemStack rejection = new ItemStack(Material.SUGAR);
                    ItemMeta rejection_meta = rejection.getItemMeta();

                    rejection_meta.setDisplayName(ChatColor.DARK_AQUA + "Отталкивание" + ChatColor.GRAY + " (30)");
                    ArrayList<String> rejection_lore = new ArrayList<>();
                    rejection_lore.add(ChatColor.WHITE + "Отбрасывает всех сущностей");
                    rejection_lore.add(ChatColor.WHITE + "в радиусе 5 блоков,");
                    rejection_lore.add(ChatColor.WHITE + "кроме соклановцев");
                    massiveHeal_lore.add("");
                    massiveHeal_lore.add(ChatColor.GREEN + "Перезарядка: 10 секунд");
                    rejection_meta.setLore(rejection_lore);

                    rejection_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    rejection.setItemMeta(rejection_meta);


                    /** Боевой клич **/
                    ItemStack battleCry = new ItemStack(Material.WOODEN_SWORD);
                    ItemMeta battleCry_meta = battleCry.getItemMeta();

                    battleCry_meta.setDisplayName(ChatColor.AQUA + "Боевой клич" + ChatColor.GRAY + " (40)");
                    ArrayList<String> battleCry_lore = new ArrayList<>();
                    battleCry_lore.add(ChatColor.WHITE + "На соклановцев в радиусе 10 блоков");
                    battleCry_lore.add(ChatColor.WHITE + "накладываются эффекты");
                    battleCry_lore.add("");
                    battleCry_lore.add(ChatColor.BLUE + "Скорость (00:10)");
                    battleCry_lore.add(ChatColor.BLUE + "Огнестойкость (00:10)");
                    battleCry_lore.add(ChatColor.BLUE + "Сила (00:10)");
                    battleCry_lore.add(ChatColor.GREEN + "Перезарядка: 12 секунд");
                    battleCry_meta.setLore(battleCry_lore);

                    battleCry_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    battleCry.setItemMeta(battleCry_meta);


                    /** Воля к жизни **/
                    ItemStack almostDead = new ItemStack(Material.TOTEM_OF_UNDYING);
                    ItemMeta almostDead_meta = almostDead.getItemMeta();

                    almostDead_meta.setDisplayName(ChatColor.GOLD + "Воля к жизни" + ChatColor.GRAY + " (50)");
                    ArrayList<String> almostDead_lore = new ArrayList<>();
                    almostDead_lore.add(ChatColor.WHITE + "При получении смертельного урона");
                    almostDead_lore.add(ChatColor.WHITE + "вы не умираете,");
                    almostDead_lore.add(ChatColor.WHITE + "а восстанавливаете 50% HP");
                    almostDead_lore.add("");
                    almostDead_lore.add(ChatColor.GREEN + "Перезарядка: 10 секунд");
                    almostDead_meta.setLore(almostDead_lore);

                    almostDead_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    almostDead.setItemMeta(almostDead_meta);


                    menu_items =  new ItemStack[]{smite, massiveHeal, regainHealth, rejection, battleCry, almostDead, null, null, null, null, massiveRegen};
                    break;


                case("tank"):
                    gui = Bukkit.createInventory(player, 18, ChatColor.BLACK + "Способности");

                    /** Врождённая броня **/
                    ItemStack naturalArmor = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                    ItemMeta naturalArmor_meta = naturalArmor.getItemMeta();

                    naturalArmor_meta.setDisplayName(ChatColor.DARK_RED + "Врождённая броня" + ChatColor.GRAY + " (0)");
                    ArrayList<String> naturalArmor_lore = new ArrayList<>();
                    naturalArmor_lore.add(ChatColor.ITALIC + "Пассивно:");
                    naturalArmor_lore.add(ChatColor.WHITE + "Вы обладаете дополнительными");
                    naturalArmor_lore.add(ChatColor.WHITE + "5 очками брони");
                    naturalArmor_meta.setLore(naturalArmor_lore);

                    naturalArmor_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    naturalArmor.setItemMeta(naturalArmor_meta);


                    /** Медлительность **/
                    ItemStack slow = getPotionItemStack(PotionType.SLOWNESS, 0, true, false, null);
                    ItemMeta slow_meta = slow.getItemMeta();

                    slow_meta.setDisplayName(ChatColor.DARK_RED + "Медлительность" + ChatColor.GRAY + " (0)");
                    ArrayList<String> slow_lore = new ArrayList<>();
                    slow_lore.add(ChatColor.ITALIC + "Пассивно:");
                    slow_lore.add(ChatColor.RED + "Медлительность (**:**)");
                    slow_meta.setLore(slow_lore);

                    slow_meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    slow.setItemMeta(slow_meta);


                    /** Дополнительное здоровье **/
                    ItemStack extraHP = new ItemStack(Material.GOLDEN_APPLE);
                    ItemMeta extraHP_meta = extraHP.getItemMeta();

                    extraHP_meta.setDisplayName(ChatColor.YELLOW + "Дополнительное здоровье" + ChatColor.GRAY + " (10)");
                    ArrayList<String> extraHP_lore = new ArrayList<>();
                    extraHP_lore.add(ChatColor.ITALIC + "Пассивно:");
                    extraHP_lore.add(ChatColor.WHITE + "Вы получаете дополнительное сердце");
                    extraHP_lore.add(ChatColor.WHITE + "к здоровью каждые 10 уровней, а не 20");
                    extraHP_meta.setLore(extraHP_lore);

                    extraHP_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    extraHP.setItemMeta(extraHP_meta);


                    /** Уменьшение отбрасывания **/
                    ItemStack reduceKnockback = new ItemStack(Material.DIAMOND_BOOTS);
                    ItemMeta reduceKnockback_meta = reduceKnockback.getItemMeta();

                    reduceKnockback_meta.setDisplayName(ChatColor.DARK_GREEN + "Сопротивление отбрасыванию " + ChatColor.GRAY + " (20)");
                    ArrayList<String> reduceKnockback_lore = new ArrayList<>();
                    reduceKnockback_lore.add(ChatColor.ITALIC + "Пассивно:");
                    reduceKnockback_lore.add(ChatColor.WHITE + "При получении урона");
                    reduceKnockback_lore.add(ChatColor.WHITE + "вас отбрасывает в 2 раза слабее");
                    reduceKnockback_meta.setLore(reduceKnockback_lore);

                    reduceKnockback_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    reduceKnockback.setItemMeta(reduceKnockback_meta);


                    /** Ядовитая кожа **/
                    ItemStack poisonedSkin = new ItemStack(Material.LEATHER);
                    ItemMeta poisonedSkin_meta = poisonedSkin.getItemMeta();

                    poisonedSkin_meta.setDisplayName(ChatColor.DARK_AQUA + "Ядовитая кожа " + ChatColor.GRAY + " (30)");
                    ArrayList<String> poisonedSkin_lore = new ArrayList<>();
                    poisonedSkin_lore.add(ChatColor.WHITE + "В течение 8 секунд");
                    poisonedSkin_lore.add(ChatColor.WHITE + "атакующие вас игроки");
                    poisonedSkin_lore.add(ChatColor.WHITE + "получают эффекты ");
                    poisonedSkin_lore.add("");
                    poisonedSkin_lore.add(ChatColor.RED + "Отравление (00:06)");
                    poisonedSkin_lore.add(ChatColor.RED + "Слабость II (00:06)");
                    poisonedSkin_lore.add(ChatColor.GREEN + "Перезарядка: 10 секунд");
                    poisonedSkin_meta.setLore(poisonedSkin_lore);

                    poisonedSkin_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    poisonedSkin.setItemMeta(poisonedSkin_meta);


                    /** Притяжение **/
                    ItemStack attraction = new ItemStack(Material.SUGAR);
                    ItemMeta attraction_meta = attraction.getItemMeta();

                    attraction_meta.setDisplayName(ChatColor.AQUA + "Притяжение " + ChatColor.GRAY + " (40)");
                    ArrayList<String> attraction_lore = new ArrayList<>();
                    attraction_lore.add(ChatColor.WHITE + "Притягивает всех сущностей");
                    attraction_lore.add(ChatColor.WHITE + "в радиусе 10 блоков,");
                    attraction_lore.add(ChatColor.WHITE + "кроме соклановцев");
                    attraction_lore.add("");
                    attraction_lore.add(ChatColor.GREEN + "Перезарядка: 10 секунд");
                    attraction_meta.setLore(attraction_lore);

                    attraction_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    attraction.setItemMeta(attraction_meta);


                    /** Очищение **/
                    ItemStack resetAndBuff = new ItemStack(Material.APPLE);
                    ItemMeta resetAndBuff_meta = resetAndBuff.getItemMeta();

                    resetAndBuff_meta.setDisplayName(ChatColor.GOLD + "Очищение " + ChatColor.GRAY + " (50)");
                    ArrayList<String> resetAndBuff_lore = new ArrayList<>();
                    resetAndBuff_lore.add(ChatColor.WHITE + "Сбрасывает с вас все дебаффы");
                    resetAndBuff_lore.add(ChatColor.WHITE + "и накладывает эффекты");
                    resetAndBuff_lore.add("");
                    resetAndBuff_lore.add(ChatColor.BLUE + "Огнестойкость (00:08)");
                    resetAndBuff_lore.add(ChatColor.BLUE + "Регенерация II (00:08)");
                    resetAndBuff_lore.add(ChatColor.BLUE + "Сопротивление II (00:08)");
                    resetAndBuff_lore.add(ChatColor.BLUE + "Скорость II (00:08)");
                    resetAndBuff_lore.add(ChatColor.GREEN + "Перезарядка: 20 секунд");
                    resetAndBuff_meta.setLore(resetAndBuff_lore);

                    resetAndBuff_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    resetAndBuff.setItemMeta(resetAndBuff_meta);


                    menu_items =  new ItemStack[]{naturalArmor, extraHP, reduceKnockback, poisonedSkin, attraction, resetAndBuff, null, null, null, slow};
                    break;


                case("warrior"):

                    /** Сила удара **/
                    ItemStack naturalStrength = new ItemStack(Material.STONE_SWORD);
                    ItemMeta naturalStrength_meta = naturalStrength.getItemMeta();

                    naturalStrength_meta.setDisplayName(ChatColor.DARK_RED + "Сила удара" + ChatColor.GRAY + " (0)");
                    ArrayList<String> naturalStrength_lore = new ArrayList<>();
                    naturalStrength_lore.add(ChatColor.ITALIC + "Пассивно:");
                    naturalStrength_lore.add(ChatColor.WHITE + "Ваш урон увеличен на 1");
                    naturalStrength_meta.setLore(naturalStrength_lore);

                    naturalStrength_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    naturalStrength.setItemMeta(naturalStrength_meta);


                    /** Рывок **/
                    ItemStack dash = new ItemStack(Material.IRON_BOOTS);
                    ItemMeta dash_meta = dash.getItemMeta();

                    dash_meta.setDisplayName(ChatColor.YELLOW + "Рывок" + ChatColor.GRAY + " (10)");
                    ArrayList<String> dash_lore = new ArrayList<>();
                    dash_lore.add(ChatColor.WHITE + "При нажатии пробела трижды");
                    dash_lore.add(ChatColor.WHITE + "вы устремляетесь вперёд");
                    dash_lore.add(ChatColor.WHITE + "и восстанавливаете 2 HP");
                    dash_lore.add("");
                    dash_lore.add(ChatColor.GREEN + "Перезарядка: 7 секунд");
                    dash_meta.setLore(dash_lore);

                    dash_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    dash.setItemMeta(dash_meta);


                    /** Пробитие блока **/
                    ItemStack shieldBreak = new ItemStack(Material.SHIELD);
                    ItemMeta shieldBreak_meta = shieldBreak.getItemMeta();

                    shieldBreak_meta.setDisplayName(ChatColor.DARK_GREEN + "Пробитие блока" + ChatColor.GRAY + " (20)");
                    ArrayList<String> shieldBreak_lore = new ArrayList<>();
                    shieldBreak_lore.add(ChatColor.ITALIC + "Пассивно:");
                    shieldBreak_lore.add(ChatColor.WHITE + "Ваша следующая атака");
                    shieldBreak_lore.add(ChatColor.WHITE + "пройдёт сквозь щит");
                    shieldBreak_lore.add("");
                    shieldBreak_lore.add(ChatColor.GREEN + "Перезарядка: 6 секунд");
                    shieldBreak_meta.setLore(shieldBreak_lore);

                    shieldBreak_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    shieldBreak.setItemMeta(shieldBreak_meta);


                    /** Усиление **/
                    ItemStack powerUp = new ItemStack(Material.GOLDEN_APPLE);
                    ItemMeta powerUp_meta = powerUp.getItemMeta();

                    powerUp_meta.setDisplayName(ChatColor.DARK_AQUA + "Усиление" + ChatColor.GRAY + " (30)");
                    ArrayList<String> powerUp_lore = new ArrayList<>();
                    powerUp_lore.add(ChatColor.WHITE + "Вы получаете эффекты");
                    powerUp_lore.add("");
                    powerUp_lore.add(ChatColor.BLUE + "Сила (00:10)");
                    powerUp_lore.add(ChatColor.BLUE + "Регенерация  (00:10)");
                    powerUp_lore.add(ChatColor.GREEN + "Перезарядка: 15 секунд");
                    powerUp_meta.setLore(powerUp_lore);

                    powerUp_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    powerUp.setItemMeta(powerUp_meta);


                    /** Иммунитет **/
                    ItemStack immunity = new ItemStack(Material.GLOWSTONE_DUST);
                    ItemMeta immunity_meta = immunity.getItemMeta();

                    immunity_meta.setDisplayName(ChatColor.AQUA + "Иммунитет" + ChatColor.GRAY + " (40)");
                    ArrayList<String> immunity_lore = new ArrayList<>();
                    immunity_lore.add(ChatColor.WHITE + "В течение 10 секунд");
                    immunity_lore.add(ChatColor.WHITE + "все наложенные на вас дебаффы");
                    immunity_lore.add(ChatColor.WHITE + "будут мгновенно сняты");
                    immunity_lore.add("");
                    immunity_lore.add(ChatColor.RED + "Свечение (00:10)");
                    immunity_lore.add(ChatColor.GREEN + "Перезарядка: 15 секунд");
                    immunity_meta.setLore(immunity_lore);

                    immunity_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    immunity.setItemMeta(immunity_meta);


                    /** Режим берсерка **/
                    ItemStack berserkMode = new ItemStack(Material.GOLDEN_AXE);
                    ItemMeta berserkMode_meta = berserkMode.getItemMeta();

                    berserkMode_meta.setDisplayName(ChatColor.GOLD + "Режим берсерка" + ChatColor.GRAY + " (50)");
                    ArrayList<String> berserkMode_lore = new ArrayList<>();
                    berserkMode_lore.add(ChatColor.ITALIC + "Пассивно:");
                    berserkMode_lore.add(ChatColor.WHITE + "При получении урона");
                    berserkMode_lore.add(ChatColor.WHITE + "при здоровье меньше 30%");
                    berserkMode_lore.add(ChatColor.WHITE + "вы получаете эффекты");
                    berserkMode_lore.add("");
                    berserkMode_lore.add(ChatColor.BLUE + "Регенерация (00:10)");
                    berserkMode_lore.add(ChatColor.BLUE + "Сила II (00:10)");
                    berserkMode_lore.add(ChatColor.BLUE + "Поглощение II (00:10)");
                    berserkMode_lore.add(ChatColor.GREEN + "Перезарядка: 20 секунд");
                    berserkMode_meta.setLore(berserkMode_lore);

                    berserkMode_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    berserkMode.setItemMeta(berserkMode_meta);


                    menu_items =  new ItemStack[]{naturalStrength, dash, shieldBreak, powerUp, immunity, berserkMode};
                    break;

                case("user"):
                    player.sendMessage(ChatColor.RED + "У вас нет класса! Используйте /choose <class>");
                    break;

                default:
                    player.sendMessage(ChatColor.RED + "Класса " + Class + " не существует.");
                    break;
            }


            gui.setContents(menu_items);
            player.openInventory(gui);
        }
            return true;
    }

    public ItemStack getPotionItemStack(PotionType type, int level, boolean extend, boolean upgraded, String displayName){
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionData(new PotionData(type, extend, upgraded));
        potion.setItemMeta(meta);
        return potion;
    }
}