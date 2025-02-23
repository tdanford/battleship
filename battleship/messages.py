
from abc import ABC, abstractmethod 
from enum import Enum 
from dataclasses import dataclass
from typing import Dict

@dataclass 
class Message: 
    source: str 
    type: str 
    payload: Dict[str, str]

class MessageTarget(ABC): 

    @abstractmethod
    def deliver_message(self, message: Message): 
        ...




