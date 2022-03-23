import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;


public class brazgovkaBot extends TelegramLongPollingBot {

    private  final ModeService modeService = ModeService.getInstance();

    @Override
    public String getBotUsername() {
        return "BrazgovkaBot";
    }

    @Override
    public String getBotToken() {
        return "5170480440:AAECWY9q9qY8c5SxH5lUTuJzf8KCjCvgbis";
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()){
            handleCalldack(update.getCallbackQuery());
        }else if (update.hasMessage()){
            handleMessage (update.getMessage());
        }
    }

@SneakyThrows
    private void handleCalldack(CallbackQuery callbackQuery) {
    Message message = callbackQuery.getMessage();
    String[] param = callbackQuery.getData().split(":");
    String action = param[0];
    Currency newCurrency = Currency.valueOf(param[1]);
    switch (action) {
        case "ORIGINAL" :
            modeService.setOriginalCurrency(message.getChatId(), newCurrency);
            break;

        case "TARGET" :
            modeService.setTargetCurrency(message.getChatId(), newCurrency);
            break;
    }
    List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
    Currency originalCurrency = modeService.getOriginalCurrency(message.getChatId());
    Currency targetCurrency = modeService.getTargetCurrency(message.getChatId());
    for (Currency currency : Currency.values()){
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(getCurrencyButton(originalCurrency, currency))
                        .callbackData("ORIGINAL:" + currency)
                        .build(),
                InlineKeyboardButton.builder()
                        .text(getCurrencyButton(targetCurrency, currency))
                        .callbackData("TARGET:" + currency)
                        .build()
        ));
    }
    execute(EditMessageReplyMarkup.builder()
            .chatId(message.getChatId().toString())
            .messageId(message.getMessageId())
            .build());
}

    @SneakyThrows
    private void handleMessage(Message message) {
        if(message.hasText() && message.hasEntities()){
            Optional<MessageEntity> commandEntity =
            message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()){
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command){
                    case "/show": /////////////////////////////
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        Currency originalCurrency = modeService.getOriginalCurrency(message.getChatId());
                        Currency targetCurrency = modeService.getTargetCurrency(message.getChatId());
                        for (Currency currency : Currency.values()){
                            buttons.add(Arrays.asList(
                                    InlineKeyboardButton.builder()
                                            .text(getCurrencyButton(originalCurrency, currency))
                                            .callbackData("ORIGINAL:" + currency)
                                            .build(),
                                    InlineKeyboardButton.builder()
                                            .text(getCurrencyButton(targetCurrency, currency))
                                            .callbackData("TARGET:" + currency)
                                            .build()
                            ));
                        }
                        execute(
                                SendMessage.builder()
                                        .text("укажите валюту")///////////////
                                        .chatId(message.getChatId().toString())
                                        .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                        .build()
                        );
                    return;
                }
            }
        }
    }

    private String getCurrencyButton(Currency saved, Currency current) {
        return saved == current ? current + "☑" : current.getDisplayName();
    }

    @SneakyThrows
    public static void main(String[] args) {
        brazgovkaBot bot = new brazgovkaBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }


}
