from abc import ABC, abstractmethod
from enum import Enum
from dataclasses import dataclass, field
from typing import Dict, List, Optional, Tuple
import logging
import time


@dataclass
class Message:
    source: str
    type: str
    payload: Dict[str, str] = field(default_factory=dict)


class MessageTarget(ABC):

    @abstractmethod
    def deliver_message(self, message: Message): ...


class NullMessageTarget(MessageTarget):

    def deliver_message(self, message):
        pass  # Eat the message


class ProxyingMessageTarget(MessageTarget):

    _internal: MessageTarget

    def __init__(self, target: Optional[MessageTarget] = None):
        self._internal = target or NullMessageTarget()

    def set_target(self, new_target: MessageTarget):
        self._internal = new_target

    def deliver_message(self, message):
        self._internal.deliver_message(message)


class RecordingMessageTarget(ProxyingMessageTarget):

    _messages: List[Tuple[float, Message]]

    def __init__(self, target: MessageTarget):
        super().__init__(target)
        self._messages = []

    def all_messages(self) -> List[Tuple[float, Message]]:
        return list(self._messages)

    def deliver_message(self, message):
        self._messages.append((time.time(), message))
        super().deliver_message(message)


class LoggingMessageTarget(ProxyingMessageTarget):

    _logger: logging.Logger

    def __init__(self, logger: logging.Logger, target: MessageTarget):
        super().__init__(target)
        self._logger = logger

    def deliver_message(self, message: Message):
        self._logger.log(logging.INFO, str(message))
        super().deliver_message(message)
