package com.cosmocats;

import org.junit.jupiter.api.Test;

class CosmoCatsApplicationTest {

    @Test
    void main_startsSpringContext() {
        // Просто запускаємо main, щоб Jacoco бачив покриття класу застосунку
        CosmoCatsApplication.main(new String[] {});
    }
}
