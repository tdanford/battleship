
import asyncio
from typing import List
from .messages import * 
import logging

class MessageQueue(MessageTarget): 

    _name: str
    _target: MessageTarget 
    _messages: List[Message]
    _logger: logging.Logger

    def __init__(self, name: str, target: MessageTarget): 
        self._name = name
        self._target = target 
        self._messages = [] 
        self._logger = logging.getLogger(name)

    def deliver_message(self, message):
        self._messages.append(message) 
    
    async def run(self): 
        processed = 0
        while True: 
            processed += await self.process()
            await asyncio.sleep(0.01)
    
    async def process(self) -> int: 
        if len(self._messages) > 0: 
            msg = self._messages.pop(0)
            self._logger.log(logging.DEBUG, f"{str(msg)}") 
            self._target.deliver_message(msg) 
            return 1 
        else: 
            return 0

